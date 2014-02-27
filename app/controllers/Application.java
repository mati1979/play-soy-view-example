package controllers;

import com.github.mati1979.play.soyplugin.plugin.Soy;
import model.IndexPageModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pagelets.HeaderPagelet;
import pagelets.WordsPagelet;
import play.mvc.Controller;
import play.mvc.Http;
import play.mvc.Result;

@Service
public class Application extends Controller {

    @Autowired
    private Soy soy;

    @Autowired
    private HeaderPagelet headerPagelet;

    @Autowired
    private WordsPagelet wordsPagelet;

    public Result index() throws Exception {
        final IndexPageModel indexPageModel = new IndexPageModel();
        indexPageModel.setHeaderModel(headerPagelet.invoke());
        indexPageModel.setWordsModel(wordsPagelet.invoke());

        return ok(soy.html("pages.index", indexPageModel));
    }

    public Result index2() throws Exception {
        final Http.Request request = request();
        final Http.Response response = response();

        final Chunks<String> chunks = new StringChunks() {

            @Override
            public void onReady(final Out<String> out) {
                try {
                    for (int i = 0; i<100; i++) {
                        final String str = soy.html(request, response, "soy.example.index2");
                        out.write(str);
                    }
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        return ok(chunks).as("text/html");
    }

    public Result index3() throws Exception {
        final IndexPageModel indexPageModel = new IndexPageModel();
        indexPageModel.setHeaderModel(headerPagelet.invoke());
        indexPageModel.setWordsModel(wordsPagelet.invoke());

        final Http.Request request = request();
        final Http.Response response = response();

        final Chunks<String> chunks = new StringChunks() {

            @Override
            public void onReady(final Out<String> out) {
                try {
                    for (int i = 0; i<100; i++) {
                        final String str = soy.html(request, response, "pages.index", indexPageModel);
                        out.write(str);
                    }
                    out.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        return ok(chunks).as("text/html");
    }

//    public class ResultStreamer {
//        public void stream(Chunks.Out<String> out) {
//            for (int i = 0; i < 10000; i++) {
//            }
//            out.close();
//        }
//    }

    public static class BuffOut {
        private StringBuilder sb;
        private Chunks.Out<String> dst;

        public BuffOut(Chunks.Out<String> dst, int bufSize) {
            this.dst = dst;
            this.sb = new StringBuilder(bufSize);
        }

        public void write(String data) {
            if ((sb.length() + data.length()) > sb.capacity()) {
                dst.write(sb.toString());
                sb.setLength(0);
            }
            sb.append(data);
        }

        public void close() {
            if (sb.length() > 0)
                dst.write(sb.toString());
            dst.close();
        }
    }

}
