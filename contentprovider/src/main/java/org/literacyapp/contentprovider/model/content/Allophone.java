package org.literacyapp.contentprovider.model.content;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.literacyapp.contentprovider.dao.converter.CalendarConverter;
import org.literacyapp.contentprovider.dao.converter.ConsonantPlaceConverter;
import org.literacyapp.contentprovider.dao.converter.ConsonantTypeConverter;
import org.literacyapp.contentprovider.dao.converter.ConsonantVoicingConverter;
import org.literacyapp.contentprovider.dao.converter.ContentStatusConverter;
import org.literacyapp.contentprovider.dao.converter.LipRoundingConverter;
import org.literacyapp.contentprovider.dao.converter.LocaleConverter;
import org.literacyapp.contentprovider.dao.converter.SoundTypeConverter;
import org.literacyapp.contentprovider.dao.converter.VowelFrontnessConverter;
import org.literacyapp.contentprovider.dao.converter.VowelHeightConverter;
import org.literacyapp.contentprovider.dao.converter.VowelLengthConverter;
import org.literacyapp.model.enums.Locale;
import org.literacyapp.model.enums.content.ContentStatus;
import org.literacyapp.model.enums.content.allophone.ConsonantPlace;
import org.literacyapp.model.enums.content.allophone.ConsonantType;
import org.literacyapp.model.enums.content.allophone.ConsonantVoicing;
import org.literacyapp.model.enums.content.allophone.LipRounding;
import org.literacyapp.model.enums.content.allophone.SoundType;
import org.literacyapp.model.enums.content.allophone.VowelFrontness;
import org.literacyapp.model.enums.content.allophone.VowelHeight;
import org.literacyapp.model.enums.content.allophone.VowelLength;

import java.util.Calendar;

/**
 * Based on {@link org.literacyapp.model.gson.content.AllophoneGson}
 */
@Entity
public class Allophone {

    @Id
    private Long id;

    @NotNull
    @Convert(converter = LocaleConverter.class, columnType = String.class)
    private Locale locale;

    @Convert(converter = CalendarConverter.class, columnType = Long.class)
    private Calendar timeLastUpdate;

    @NotNull
    private Integer revisionNumber; // [1, 2, 3, ...]

    @NotNull
    @Convert(converter = ContentStatusConverter.class, columnType = String.class)
    private ContentStatus contentStatus;

    @NotNull
    private String valueIpa; // IPA - International Phonetic Alphabet

    @NotNull
    private String valueSampa; // X-SAMPA - Extended Speech Assessment Methods Phonetic Alphabet

    @Convert(converter = SoundTypeConverter.class, columnType = String.class)
    private SoundType soundType;

    @Convert(converter = VowelLengthConverter.class, columnType = String.class)
    private VowelLength vowelLength;

    @Convert(converter = VowelHeightConverter.class, columnType = String.class)
    private VowelHeight vowelHeight;

    @Convert(converter = VowelFrontnessConverter.class, columnType = String.class)
    private VowelFrontness vowelFrontness;

    @Convert(converter = LipRoundingConverter.class, columnType = String.class)
    private LipRounding lipRounding;

    @Convert(converter = ConsonantTypeConverter.class, columnType = String.class)
    private ConsonantType consonantType;

    @Convert(converter = ConsonantPlaceConverter.class, columnType = String.class)
    private ConsonantPlace consonantPlace;

    @Convert(converter = ConsonantVoicingConverter.class, columnType = String.class)
    private ConsonantVoicing consonantVoicing;

    @Generated(hash = 1607902866)
    public Allophone(Long id, @NotNull Locale locale, Calendar timeLastUpdate,
            @NotNull Integer revisionNumber, @NotNull ContentStatus contentStatus,
            @NotNull String valueIpa, @NotNull String valueSampa, SoundType soundType,
            VowelLength vowelLength, VowelHeight vowelHeight, VowelFrontness vowelFrontness,
            LipRounding lipRounding, ConsonantType consonantType, ConsonantPlace consonantPlace,
            ConsonantVoicing consonantVoicing) {
        this.id = id;
        this.locale = locale;
        this.timeLastUpdate = timeLastUpdate;
        this.revisionNumber = revisionNumber;
        this.contentStatus = contentStatus;
        this.valueIpa = valueIpa;
        this.valueSampa = valueSampa;
        this.soundType = soundType;
        this.vowelLength = vowelLength;
        this.vowelHeight = vowelHeight;
        this.vowelFrontness = vowelFrontness;
        this.lipRounding = lipRounding;
        this.consonantType = consonantType;
        this.consonantPlace = consonantPlace;
        this.consonantVoicing = consonantVoicing;
    }

    @Generated(hash = 1168789856)
    public Allophone() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Locale getLocale() {
        return this.locale;
    }

    public void setLocale(Locale locale) {
        this.locale = locale;
    }

    public Calendar getTimeLastUpdate() {
        return this.timeLastUpdate;
    }

    public void setTimeLastUpdate(Calendar timeLastUpdate) {
        this.timeLastUpdate = timeLastUpdate;
    }

    public Integer getRevisionNumber() {
        return this.revisionNumber;
    }

    public void setRevisionNumber(Integer revisionNumber) {
        this.revisionNumber = revisionNumber;
    }

    public ContentStatus getContentStatus() {
        return this.contentStatus;
    }

    public void setContentStatus(ContentStatus contentStatus) {
        this.contentStatus = contentStatus;
    }

    public String getValueIpa() {
        return this.valueIpa;
    }

    public void setValueIpa(String valueIpa) {
        this.valueIpa = valueIpa;
    }

    public String getValueSampa() {
        return this.valueSampa;
    }

    public void setValueSampa(String valueSampa) {
        this.valueSampa = valueSampa;
    }

    public SoundType getSoundType() {
        return this.soundType;
    }

    public void setSoundType(SoundType soundType) {
        this.soundType = soundType;
    }

    public VowelLength getVowelLength() {
        return this.vowelLength;
    }

    public void setVowelLength(VowelLength vowelLength) {
        this.vowelLength = vowelLength;
    }

    public VowelHeight getVowelHeight() {
        return this.vowelHeight;
    }

    public void setVowelHeight(VowelHeight vowelHeight) {
        this.vowelHeight = vowelHeight;
    }

    public VowelFrontness getVowelFrontness() {
        return this.vowelFrontness;
    }

    public void setVowelFrontness(VowelFrontness vowelFrontness) {
        this.vowelFrontness = vowelFrontness;
    }

    public LipRounding getLipRounding() {
        return this.lipRounding;
    }

    public void setLipRounding(LipRounding lipRounding) {
        this.lipRounding = lipRounding;
    }

    public ConsonantType getConsonantType() {
        return this.consonantType;
    }

    public void setConsonantType(ConsonantType consonantType) {
        this.consonantType = consonantType;
    }

    public ConsonantPlace getConsonantPlace() {
        return this.consonantPlace;
    }

    public void setConsonantPlace(ConsonantPlace consonantPlace) {
        this.consonantPlace = consonantPlace;
    }

    public ConsonantVoicing getConsonantVoicing() {
        return this.consonantVoicing;
    }

    public void setConsonantVoicing(ConsonantVoicing consonantVoicing) {
        this.consonantVoicing = consonantVoicing;
    }
}
