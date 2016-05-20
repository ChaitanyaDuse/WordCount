package com.sample.wordcount.wordcount;

class WordOccurrenceComparable implements Comparable<WordOccurrenceComparable> {
    public String wordFromFile;
    public int numberOfOccurrence;

    public WordOccurrenceComparable(String wordFromFile, int numberOfOccurrence) {
        super();
        this.wordFromFile = wordFromFile;
        this.numberOfOccurrence = numberOfOccurrence;
    }


    @Override
    public int compareTo(WordOccurrenceComparable arg0) {
        int crunchifyCompare = Integer.compare(arg0.numberOfOccurrence, this.numberOfOccurrence);
        return crunchifyCompare != 0 ? crunchifyCompare : wordFromFile.compareTo(arg0.wordFromFile);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
        //// TODO: 5/18/2016  implement hashcode for better performance


    }

    @Override
    public boolean equals(Object crunchifyObj) {
        return  super.equals(crunchifyObj);
        //// TODO: 5/18/2016  implement hashcode for better performance

    }
}