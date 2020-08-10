import com.tsgz.server.parser.YarnUIParser;
import com.tsgz.common.util.ResourceUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @Author: liqin
 * @Description:
 * @Date: Created in 16:39 2020/8/4
 * @Modified By:
 */
public class SparkAppInfoTest {
    public static void main(String[] args) {
        String url = "http://172.16.140.151:8088/cluster/apps/RUNNING";
        try {
            Document doc = Jsoup.connect(url).get();
            YarnUIParser.getInstance().parseUI(doc, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private static String getHttpResponse(String url) {
        HttpURLConnection conn = null;
        OutputStream out = null;
        InputStream in = null;

        StringBuilder buffer = new StringBuilder();
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
//            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setConnectTimeout(50000);
            conn.setReadTimeout(50000);

            conn.setRequestMethod("GET");
            conn.connect();
            int resCode = conn.getResponseCode();
            String message = conn.getResponseMessage();
            System.out.println(resCode);

            if ("OK".equals(message)) {
                in = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));

                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
            ResourceUtil.close(in, out);
        }
        return buffer.toString();
    }
}
