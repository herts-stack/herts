package org.herts.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.herts.example.bidstreaming_rpc.client.BiStreamingClient;
import org.herts.example.clientstreaming_rpc.client.ClientStreamingClient;
import org.herts.common.context.HertsType;
import org.herts.example.bidstreaming_rpc.server.BiStreamingServer;
import org.herts.example.clientstreaming_rpc.server.ClientStreamingServer;
import org.herts.example.reactivestreaming_rpc.client.ReactiveStreamingClient;
import org.herts.example.reactivestreaming_rpc.server.ReactiveStreamingServer;
import org.herts.example.http.client.HttpClient;
import org.herts.example.http.server.HttpServer;
import org.herts.example.serverstreaming_rpc.client.ServerStreamingClient;
import org.herts.example.serverstreaming_rpc.server.ServerStreamingServer;
import org.herts.example.unary_rpc.client.UnaryClient;
import org.herts.example.unary_rpc.server.UnaryServer;

public class Main {
    private static final String ExecTypeMsg = "`server` or `client`";
    private static final String HertsTypeMsg = "`unary` or `server_streaming` or `client_streaming` or `bidirectional_streaming` or `reactive_streaming`";
    public static void main(String[] args) throws InterruptedException, JsonProcessingException {
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

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd;
        String exec_type = null;
        String herts_type = null;
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

        } catch (ParseException pe) {
            System.out.println("Error parsing command-line arguments! " + pe.getMessage());
            System.exit(1);
        }
        if (exec_type == null || herts_type == null) {
            System.out.println("Error parsing command-line arguments!");
            System.exit(1);
        }

        HertsType coreType = ArgOperation.convert(herts_type);
        if (exec_type.equals("server")) {
            switch (coreType) {
                case Unary -> UnaryServer.run();
                case ClientStreaming -> ClientStreamingServer.run();
                case ServerStreaming -> ServerStreamingServer.run();
                case BidirectionalStreaming -> BiStreamingServer.run();
                case Reactive -> ReactiveStreamingServer.run();
                case Http -> HttpServer.run();
            }
        } else {
            try {
                switch (coreType) {
                    case Unary -> UnaryClient.run();
                    case ClientStreaming -> ClientStreamingClient.run();
                    case ServerStreaming -> ServerStreamingClient.run();
                    case BidirectionalStreaming -> BiStreamingClient.run();
                    case Reactive -> ReactiveStreamingClient.run();
                    case Http -> HttpClient.run();
                }
                Thread.sleep(5000);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }
        }
    }
}