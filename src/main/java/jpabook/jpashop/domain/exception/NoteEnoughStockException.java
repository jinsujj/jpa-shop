package jpabook.jpashop.domain.exception;

public class NoteEnoughStockException extends RuntimeException {
    public NoteEnoughStockException(String need_more_stock) {
        super(need_more_stock);
    }
}
