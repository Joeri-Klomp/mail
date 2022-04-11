package be.vdab.mail.exceptions;

public class KanMailNietZendenException extends RuntimeException {
    private static final long serialVersionUID = 1L;
    public KanMailNietZendenException(Exception oorspronkekelijkeFout) {
        super(oorspronkekelijkeFout);
    }
}
