package com.example.byitscover.helpers;

/**
 * An <it>ISBN,</it> or International Standard Book Number, is a value which uniquely identifies a book.
 * A <it>representation</it> is a string which represents an ISBN.
 * Representations are not unique, and they may be either ISBN-10 or ISBN-13.
 * Every book has an ISBN-13 representation, but not all books have ISBN-10 representations.
 * For example, 9780262033848 and 0-262-03384-4 are different representations of the same ISBN.
 * This class is an ISBN, and it provides facilities for handling representations of ISBNs.
 * All instances of this class must be instantiated from valid ISBN representations.
 * The rest of the codebase may compare ISBNs without concern for the underlying representations.
 *
 * @author Jack
 * @version 1.0
 */

// TODO: Add unit tests

public final class Isbn {
    // The canonical representation for the purposes of this class is ISBN-13
    private final String canonicalRepresentation;

    private static int isbn10Sum(String head) {
        int checksum = 0;

        for (int i = 0; i < head.length(); i++) {
            int digit = head.charAt(i) - '0';
            checksum = (checksum + (10 - i) * digit) % 11;
        }

        return checksum;
    }

    /**
     * Tests whether input string is an ISBN-10 representation.
     */
    public static boolean isIsbn10(String representation) {
        long number;
        int complement;

        representation = stripSeparators(representation);

        if (representation == null)
            return false;

        try {
            number = Long.parseLong(representation.substring(0, representation.length() - 1));

            if (representation.charAt(representation.length() - 1) == 'X')
                complement = 10;
            else
                complement = Integer.parseInt(representation.substring(representation.length() - 1));
        } catch (NumberFormatException ex) {
            return false;
        }

        if (representation.length() != 10 || number < 0)
            return false;

        int checksum = isbn10Sum(representation.substring(0, representation.length() - 1));
        return  (checksum + complement) % 11 == 0;
    }

    private static int isbn13Sum(String head) {
        int checksum = 0;

        for (int i = 0; i < head.length(); i++) {
            int digit = head.charAt(i) - '0';
            int multiplier = i % 2 == 0 ? 1 : 3;
            checksum = (checksum + multiplier * digit) % 10;
        }

        return checksum;
    }

    /**
     * Tests whether input string is an ISBN-13 representation.
     */
    public static boolean isIsbn13(String representation) {
        long number;
        int complement;

        representation = stripSeparators(representation);

        if (representation == null)
            return false;

        try {
            number = Long.parseLong(representation.substring(0, representation.length() - 1));
            complement = Integer.parseInt(representation.substring(representation.length() - 1));
        } catch (NumberFormatException ex) {
            return false;
        }

        if (representation.length() != 13 || number < 0)
            return false;

        int checksum = isbn13Sum(representation.substring(0, representation.length() - 1));
        return  (checksum + complement) % 10 == 0;
    }

    /**
     * Tests whether input string is an ISBN representation.
     */
    public static boolean isIsbn(String representation) {
        return isIsbn10(representation) || isIsbn13(representation);
    }

    /**
     * Converts ISBN-10 representation into ISBN-13 representation.
     * If representation is not an ISBN-10 representation, IllegalArgumentException is thrown.
     */
    public static String isbn10ToIsbn13(String representation) {
        if (!isIsbn10(representation))
            throw new IllegalArgumentException("\"" + representation + "\" is not an ISBN-10 representation");

        String head = "978" + representation.substring(0, representation.length() - 1);
        int checksum = isbn13Sum(head);
        int complement = (10 - checksum) % 10;

        return head + checksum;
    }

    private static String stripSeparators(String representation) {
        // TODO: validate that string with separators is ISBN representation
        return representation.replaceAll("[-| ]", "");
    }

    /**
     * Constructs an ISBN from a valid ISBN representation.
     * If the representation is not valid, then IllegalArgumentException is thrown.
     * Ensure that representation is correct by isIsbn() first.
     */
    public Isbn(String representation) {
        if (isIsbn10(representation))
            canonicalRepresentation = isbn10ToIsbn13(representation);
        else if (isIsbn13(representation))
            canonicalRepresentation = stripSeparators(representation);
        else
            throw new IllegalArgumentException("\"" + representation + "\" is not a valid representation of an ISBN");
    }

    /**
     * Checks if ISBN has ISBN-10 representation.
     */
    public boolean hasIsbn10Representation() {
        return canonicalRepresentation.substring(0, 3).equals("978");
    }

    /**
     * Constructs an ISBN from a valid ISBN representation.
     * If the representation is not valid, then IllegalArgumentException is thrown.
     * Ensure that representation is correct by isIsbn() first.
     */
    public String isbn10Representation() {
        if (!hasIsbn10Representation())
            throw new IllegalArgumentException("\"" + this + "\" is not an ISBN-10");

        String head = canonicalRepresentation.substring(3, canonicalRepresentation.length() - 1);
        int checksum = isbn10Sum(head);
        int complement = (11 - checksum) % 11;

        if (complement == 10)
            return head + 'X';
        else
            return head + complement;
    }

    /**
     * Gets ISBN-13 representation of ISBN
     */
    public String isbn13Representation() {
        return canonicalRepresentation;
    }

    public String toString() {
        return isbn13Representation();
    }

    public int hashCode() {
        return canonicalRepresentation.hashCode();
    }

    public boolean equals(Isbn other) {
        return canonicalRepresentation.equals(other.canonicalRepresentation);
    }
    
    public Isbn clone() {
        return new Isbn(canonicalRepresentation);
    }
}
