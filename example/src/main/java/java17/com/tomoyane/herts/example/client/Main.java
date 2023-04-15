package java17.com.tomoyane.herts.example.client;

import com.tomoyane.herts.ArgCollector;
import com.tomoyane.herts.ClientHttpServiceExample;
import com.tomoyane.herts.ClientServiceExample;
import com.tomoyane.herts.hertscommon.context.HertsType;

public class Main {
    public static void main(String[] args) {
        if (!ArgCollector.isOk(args[0])) {
            System.out.println("Failed to set args. Allowed " + String.valueOf(ArgCollector.allowedArgs));
            return;
        }

        HertsType coreType = ArgCollector.convert(args[0]);
        try {
            switch (coreType) {
                case Unary -> ClientServiceExample.unary();
                case ClientStreaming -> ClientServiceExample.clientStreaming();
                case ServerStreaming -> ClientServiceExample.serverStreaming();
                case BidirectionalStreaming -> ClientServiceExample.bidirectionalStreaming();
                case Http -> ClientHttpServiceExample.client();
            }
            Thread.sleep(5000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}