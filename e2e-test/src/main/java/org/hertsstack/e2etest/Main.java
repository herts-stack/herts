package org.hertsstack.e2etest;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.hertsstack.e2etest.gateway.GwClient;
import org.hertsstack.e2etest.gateway.GwServer;
import org.hertsstack.e2etest.http.client.HttpClient;
import org.hertsstack.e2etest.reactivestreaming_rpc.client.IntegrationTestRsClient;
import org.hertsstack.e2etest.reactivestreaming_rpc.client.QueueTestRsClient;
import org.hertsstack.e2etest.reactivestreaming_rpc.server.IntegrationTestRsServer;
import org.hertsstack.e2etest.reactivestreaming_rpc.server.QueueTestRsServer;
import org.hertsstack.e2etest.serverstreaming_rpc.client.ServerStreamingClient;
import org.hertsstack.e2etest.serverstreaming_rpc.server.ServerStreamingServer;
import org.hertsstack.e2etest.bidstreaming_rpc.client.BiStreamingClient;
import org.hertsstack.e2etest.clientstreaming_rpc.client.ClientStreamingClient;
import org.hertsstack.core.context.HertsType;
import org.hertsstack.e2etest.bidstreaming_rpc.server.BiStreamingServer;
import org.hertsstack.e2etest.clientstreaming_rpc.server.ClientStreamingServer;
import org.hertsstack.e2etest.http.server.HttpServer;
import org.hertsstack.e2etest.unary_rpc.client.UnaryClient;
import org.hertsstack.e2etest.unary_rpc.server.UnaryServer;
import org.hertsstack.serializer.MessageJsonParsingException;

public class Main {
    private static final String ExecTypeMsg = "`server` or `client` or `gateway` or `gateway_client`";
    private static final String HertsTypeMsg = "`unary` or `server_streaming` or `client_streaming` or `bidirectional_streaming` or `reactive_streaming`";
    public static void main(String[] args) throws Exception {
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

        options.addOption(Option.builder("r")
                .longOpt("redis_host")
                .hasArg(true)
                .desc("")
                .required(false)
                .build());

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        String exec_type = null, herts_type = null, test_type = null, redis_host = null;
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
            if (cmd.hasOption("r")) {
                redis_host = cmd.getOptionValue("r");
                if (redis_host == null || redis_host.isEmpty()) {
                    throw new ParseException("redis_host");
                }
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
        if (exec_type.equals(ArgOperation.GATEWAY_SERVER)) {
            GwServer.run();
            return;
        } else if (exec_type.equals(ArgOperation.GATEWAY_CLIENT)) {
            GwClient.run();
            return;
        }

        if (exec_type.equals(ArgOperation.SERVER) && test_type.equals(ArgOperation.testTypes[0])) {
            switch (coreType) {
                case Unary:
                    UnaryServer.run();
                    break;
                case ClientStreaming:
                    ClientStreamingServer.run();
                    break;
                case ServerStreaming:
                    ServerStreamingServer.run();
                    break;
                case BidirectionalStreaming:
                    BiStreamingServer.run();
                    break;
                case Reactive:
                    IntegrationTestRsServer.run();
                    break;
                case Http:
                    HttpServer.run();
                    break;
            }
        } else if (exec_type.equals(ArgOperation.SERVER) && test_type.equals(ArgOperation.testTypes[1])) {
            switch (coreType) {
                case Reactive:
                    QueueTestRsServer.run(redis_host);
                    break;
            }
        } else if (exec_type.equals(ArgOperation.CLIENT) && test_type.equals(ArgOperation.testTypes[0])) {
            Thread thread = new Thread(() -> {
                switch (coreType) {
                    case Unary:
                        //AutoReconnectUnaryClient.run();
                        UnaryClient.run();
                        break;
                    case ClientStreaming:
//                        AutoReconnectClientStreamingClient.run();
                        ClientStreamingClient.run();
                        break;
                    case ServerStreaming:
                        ServerStreamingClient.run();
                        break;
                    case BidirectionalStreaming:
                        BiStreamingClient.run();
                        break;
                    case Reactive:
                        IntegrationTestRsClient.run();
                        break;
                    case Http:
                        try {
                            HttpClient.run();
                        } catch (MessageJsonParsingException e) {
                            throw new RuntimeException(e);
                        }
                        break;
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
                    case Reactive:
                        QueueTestRsClient.run();
                        break;
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