package com.example.byitscover.helpers;
import com.example.byitscover.helpers.Isbn;
import org.junit.Test;
import static org.junit.Assert.*;

public class IsbnTest {
    @Test
    public void validIsbn10() {
        assertFalse(Isbn.isIsbn10("3-12907865-X"));
        assertTrue(Isbn.isIsbn10("0-8044-2957-X"));
        assertFalse(Isbn.isIsbn10("3 12907865 X"));
        assertTrue(Isbn.isIsbn10("0 8044 2957 X"));
        assertTrue(Isbn.isIsbn10("080442957X"));
        assertTrue(Isbn.isIsbn10("0-85131-041-9"));
        assertTrue(Isbn.isIsbn10("0 85131 041 9"));
        assertFalse(Isbn.isIsbn10("978-3-16-148410-0"));
        assertFalse(Isbn.isIsbn10("978 3 16 148410 0"));
        assertFalse(Isbn.isIsbn10("9783161484100"));
    }

    @Test
    public void validIsbn13() {
        assertFalse(Isbn.isIsbn13("3-12907865654-X"));
        assertFalse(Isbn.isIsbn13("3-12907865654-43"));
        assertFalse(Isbn.isIsbn13("3 12907865654 X"));
        assertFalse(Isbn.isIsbn13("3 12907865654 43"));
        assertFalse(Isbn.isIsbn13("31290786565443"));
        assertTrue(Isbn.isIsbn13("978-3-16-148410-0"));
        assertTrue(Isbn.isIsbn13("978 3 16 148410 0"));
        assertTrue(Isbn.isIsbn13("9783161484100"));
        assertFalse(Isbn.isIsbn13("0-85131-041-9"));
        assertFalse(Isbn.isIsbn13("0851310419"));
        assertFalse(Isbn.isIsbn13("0 85131 041 9"));
    }

    @Test
    public void isbnRepresentationConversion() {
        assertTrue(new Isbn("0-85131-041-9").hasIsbn10Representation());

        assertTrue(new Isbn("0-85131-041-9").isbn10Representation().equals("0851310419"));
        assertTrue(new Isbn("0-85131-041-9").isbn13Representation().equals("9780851310411"));

        assertFalse(new Isbn("977-3-26-148410-0").hasIsbn10Representation());
        assertTrue(new Isbn("977-3-26-148410-0").isbn13Representation().equals("9773261484100"));

        assertTrue(new Isbn("978-0-85131-041-1").hasIsbn10Representation());
        assertTrue(new Isbn("978-0-85131-041-1").isbn10Representation().equals("0851310419"));
        assertTrue(new Isbn("978-0-85131-041-1").isbn13Representation().equals("9780851310411"));
    }

    @Test
    public void isbnComparison() {
        assertTrue(new Isbn("0-85131-041-9").equals(new Isbn("0851310419")));
        assertTrue(new Isbn("0-85131-041-9").equals(new Isbn("9780851310411")));
        assertFalse(new Isbn("0-85131-041-9").equals(new Isbn("9773261484100")));
    }
}
