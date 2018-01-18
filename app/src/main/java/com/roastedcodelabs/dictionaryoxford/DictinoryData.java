package com.roastedcodelabs.dictionaryoxford;

import java.util.ArrayList;

/**
 * Created by kuldeep on 17/1/18.
 */

public class DictinoryData {
    private String word,deriv_of_wrd,mean_wrd,audio_url;
   private ArrayList<String> examples;

public   DictinoryData(String mword,String mderiv_of_wrd,String mmean_wrd,ArrayList<String> mexamples,String maudio_url)
    {
               word=mword;
               deriv_of_wrd=mderiv_of_wrd;
               mean_wrd=mmean_wrd;
               audio_url=maudio_url;
               examples=mexamples;
    }

    public String getDeriv_of_wrd() {
        return deriv_of_wrd;
    }

    public String getMean_wrd() {
        return mean_wrd;
    }

    public String getWord() {
        return word;
    }

    public ArrayList<String> getExamples() {
        return examples;
    }
    public String getAudio_url()
    {
        return audio_url;
    }
}
