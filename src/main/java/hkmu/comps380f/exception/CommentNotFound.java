package hkmu.comps380f.exception;

import java.util.UUID;

public class CommentNotFound extends Exception {
    public CommentNotFound(String comment) {
        super("Comment " + comment + " does not found.");
    }
}
