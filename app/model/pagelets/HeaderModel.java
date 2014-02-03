package model.pagelets;

/**
 * Created by mati on 03/02/2014.
 */
public class HeaderModel {

    private String jQueryLink;
    private String title;
    private String cssLink;
    private String soyUtilsLink;

    public String getSoyUtilsLink() {
        return soyUtilsLink;
    }

    public void setSoyUtilsLink(String soyUtilsLink) {
        this.soyUtilsLink = soyUtilsLink;
    }

    public String getjQueryLink() {
        return jQueryLink;
    }

    public void setjQueryLink(String jQueryLink) {
        this.jQueryLink = jQueryLink;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCssLink() {
        return cssLink;
    }

    public void setCssLink(String cssLink) {
        this.cssLink = cssLink;
    }

}
