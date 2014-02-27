package helper;

import play.mvc.Results;

public class BufOut {

    private StringBuilder sb;

    private Results.Chunks.Out<String> dst;

    public BufOut(Results.Chunks.Out<String> dst, int bufSize) {
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
