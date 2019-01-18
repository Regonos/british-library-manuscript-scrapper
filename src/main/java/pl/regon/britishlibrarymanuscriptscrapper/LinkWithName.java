package pl.regon.britishlibrarymanuscriptscrapper;

public class LinkWithName {

    private final String link;

    private final String name;

    public LinkWithName(String link, String name) {
        this.link = link;
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }
}
