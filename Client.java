//import java.io.DataInputStream;
//import java.io.FileOutputStream;
//import java.io.IOException;
//import java.net.Socket;
//
//public class Client {
//    public static void main(String[] args){
//        Socket sock = new Socket(serverIp, 9999);
//        DataInputStream dis = null;
//        while (true){//changeCondition
//            try {
//                dis = new DataInputStream(sock.getInputStream());
//                FileOutputStream fout = new FileOutputStream("frameImage.jpg");
//                int i;
//                while ( (i = dis.read()) > -1) {
//                    fout.write(i);
//                }
//
//                fout.flush();
//                fout.close();
//                dis.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            sock.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
