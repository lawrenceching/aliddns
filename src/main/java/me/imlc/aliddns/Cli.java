package me.imlc.aliddns;

import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Cli {

    private static Logger logger = LoggerFactory.getLogger(Cli.class);

    public static void main (String[] args) {

        Options options = new Options();
        options.addOption("d", "daemon", false, "Run in daemon mode");
        options.addOption("t", "interval", true, "Interval in seconds");

        options.addOption(Option.builder()
                .longOpt("accessKeyId")
                .hasArg()
                .desc("Aliyun Access Key Id")
                .numberOfArgs(1)
                .required()
                .build());

        options.addOption(Option.builder()
                .longOpt("accessKeySecret")
                .hasArg()
                .desc("Aliyun Access Key Secret")
                .numberOfArgs(1)
                .required()
                .build());

        options.addOption(Option.builder()
                .longOpt("rr")
                .hasArg()
                .desc("Record, in domain \"www.example.com\", record is \"www\"")
                .numberOfArgs(1)
                .required()
                .build());

        options.addOption(Option.builder()
                .longOpt("domain")
                .hasArg()
                .desc("Domain, in domain \"www.example.com\", domain is \"example.com\"")
                .numberOfArgs(1)
                .required()
                .build());

        options.addOption(Option.builder()
                .longOpt("type")
                .hasArg()
                .desc("Domain type")
                .numberOfArgs(1)
                .required()
                .build());

        options.addOption(Option.builder()
                .longOpt("ip")
                .hasArg()
                .desc("ip")
                .numberOfArgs(1)
                .required(false)
                .build());

        if(args.length == 0) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("aliddns", options);
            return;
        }

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);

            String id = commandLine.getOptionValue("accessKeyId");
            String secret = commandLine.getOptionValue("accessKeySecret");
            String rr = commandLine.getOptionValue("rr");
            String domain = commandLine.getOptionValue("domain");
            String type = commandLine.getOptionValue("type");
            Optional<String> defaultIp = Optional.ofNullable(commandLine.getOptionValue("ip"));
            String interval = commandLine.getOptionValue("interval", String.valueOf(TimeUnit.MINUTES.toSeconds(5)));

            logger.debug("Use id \"{}\" and secret \"{}\"", id ,secret);

            Utils u = new Utils();

            Runnable setDns = () -> {
                try {
                    String ip = defaultIp.orElseGet(() -> u.detectPublicIp());
                    AliDns aliDns = new AliDns(id, secret);
                    aliDns.setDns(rr, domain, ip, type);
                } catch (Throwable e) {
                    logger.error("Failed to update Aliyun DNS", e);
                }
            };

            if(commandLine.hasOption("d")) {
                logger.info("Started aliddns in daemon mode");
                logger.info("aliddns will try to update defaultIp every {} seconds", interval);

                ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
                executor.scheduleAtFixedRate(setDns, 0, Long.parseLong(interval), TimeUnit.SECONDS);

            } else {
                setDns.run();
            }

        } catch (ParseException e) {
            logger.error("Unable tp parse command line argument", e);

            System.out.println();

            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("aliddns", options);
        }


    }

}
