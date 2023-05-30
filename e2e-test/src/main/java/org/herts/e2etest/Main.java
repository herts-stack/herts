package org.herts.e2etest;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.herts.e2etest.bidstreaming_rpc.client.BiStreamingClient;
import org.herts.e2etest.clientstreaming_rpc.client.ClientStreamingClient;
import org.herts.common.context.HertsType;
import org.herts.e2etest.bidstreaming_rpc.server.BiStreamingServer;
import org.herts.e2etest.clientstreaming_rpc.server.ClientStreamingServer;
import org.herts.e2etest.reactivestreaming_rpc.client.IntegrationTestRsClient;
import org.herts.e2etest.reactivestreaming_rpc.client.QueueTestRsClient;
import org.herts.e2etest.reactivestreaming_rpc.server.IntegrationTestRsServer;
import org.herts.e2etest.http.client.HttpClient;
import org.herts.e2etest.http.server.HttpServer;
import org.herts.e2etest.reactivestreaming_rpc.server.QueueTestRsServer;
import org.herts.e2etest.serverstreaming_rpc.client.ServerStreamingClient;
import org.herts.e2etest.serverstreaming_rpc.server.ServerStreamingServer;
import org.herts.e2etest.unary_rpc.client.UnaryClient;
import org.herts.e2etest.unary_rpc.server.UnaryServer;

public class Main {
    private static final String ExecTypeMsg = "`server` or `client`";
    private static final String HertsTypeMsg = "`unary` or `server_streaming` or `client_streaming` or `bidirectional_streaming` or `reactive_streaming`";
    public static void main(String[] args) {
        Options options = new Options();
        options.addOption(Option.builder("e")
                .longOpt("exec_type")
                .hasArg(true)
                .desc(ExecTypeMsg)
                .required(true)
                .build());

        options.addOption(Option.builder("h")
                .longOpt("herts_type")
                .hasArg(true)
                .desc(HertsTypeMsg)
                .required(true)
                .build());

        options.addOption(Option.builder("t")
                .longOpt("test_type")
                .hasArg(true)
                .desc(HertsTypeMsg)
                .required(false)
                .build());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        String exec_type = null, herts_type = null, test_type = null;
        try {
            cmd = parser.parse(options, args);
            if (cmd.hasOption("e")) {
                exec_type = cmd.getOptionValue("e");
                if (!ArgOperation.isExecuteTypeOk(exec_type)) {
                    throw new ParseException(ExecTypeMsg);
                }
            }
            if (cmd.hasOption("h")) {
                herts_type = cmd.getOptionValue("h");
                if (!ArgOperation.isHertsTypeOk(herts_type)) {
                    throw new ParseException(HertsTypeMsg);
                }
            }
            if (cmd.hasOption("t")) {
                test_type = cmd.getOptionValue("t");
                if (!ArgOperation.isTestTypeOk(test_type)) {
                    throw new ParseException(HertsTypeMsg);
                }
            } else {
                test_type = ArgOperation.testTypes[0];
            }
        } catch (ParseException pe) {
            System.out.println("Error parsing command-line arguments! " + pe.getMessage());
            System.exit(1);
        }
        if (exec_type == null || herts_type == null) {
            System.out.println("Error parsing command-line arguments!");
            System.exit(1);
        }

        HertsType coreType = ArgOperation.convert(herts_type);
        if (exec_type.equals(ArgOperation.SERVER) && test_type.equals(ArgOperation.testTypes[0])) {
            switch (coreType) {
                case Unary -> UnaryServer.run();
                case ClientStreaming -> ClientStreamingServer.run();
                case ServerStreaming -> ServerStreamingServer.run();
                case BidirectionalStreaming -> BiStreamingServer.run();
                case Reactive -> IntegrationTestRsServer.run();
                case Http -> HttpServer.run();
            }
        } else if (exec_type.equals(ArgOperation.SERVER) && test_type.equals(ArgOperation.testTypes[1])) {
            switch (coreType) {
                case Reactive -> QueueTestRsServer.run();
            }
        } else if (exec_type.equals(ArgOperation.CLIENT) && test_type.equals(ArgOperation.testTypes[0])) {
            Thread thread = new Thread(() -> {
                switch (coreType) {
                    case Unary -> UnaryClient.run();
                    case ClientStreaming -> ClientStreamingClient.run();
                    case ServerStreaming -> ServerStreamingClient.run();
                    case BidirectionalStreaming -> BiStreamingClient.run();
                    case Reactive -> IntegrationTestRsClient.run();
                    case Http -> HttpClient.run();
                }

            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if (exec_type.equals(ArgOperation.CLIENT) && test_type.equals(ArgOperation.testTypes[1])) {
            Thread thread = new Thread(() -> {
                switch (coreType) {
                    case Reactive -> QueueTestRsClient.run();
                }
            });
            thread.start();
            try {
                thread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}