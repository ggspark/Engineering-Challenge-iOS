package play.gaurav.holmusk.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * @author Gaurav Gupta <gaurav@thegauravgupta.com>
 * @since 27/May/2015

    private String fibre;
 */public class Meta extends RealmObject{

    @Expose
    private String fibre;
    @SerializedName("polyunsaturated fat")
    @Expose
    private String polyunsaturatedFat;
    @Expose
    private String sodium;
    @Expose
    private String energy;
    @Expose
    private String cholesterol;
    @Expose
    private String fat;
    @Expose
    private String sugar;
    @Expose
    private String carbohydrate;
    @SerializedName("saturated fat")
    @Expose
    private String saturatedFat;
    @Expose
    private String portion;
    @SerializedName("monounsaturated fat")
    @Expose
    private String monounsaturatedFat;
    @Expose
    private String protein;
    @Expose
    private String potassium;

    /**
     *
     * @return
     * The fibre
     */
    public String getFibre() {
        return fibre;
    }

    /**
     *
     * @param fibre
     * The fibre
     */
    public void setFibre(String fibre) {
        this.fibre = fibre;
    }

    /**
     *
     * @return
     * The polyunsaturatedFat
     */
    public String getPolyunsaturatedFat() {
        return polyunsaturatedFat;
    }

    /**
     *
     * @param polyunsaturatedFat
     * The polyunsaturated fat
     */
    public void setPolyunsaturatedFat(String polyunsaturatedFat) {
        this.polyunsaturatedFat = polyunsaturatedFat;
    }

    /**
     *
     * @return
     * The sodium
     */
    public String getSodium() {
        return sodium;
    }

    /**
     *
     * @param sodium
     * The sodium
     */
    public void setSodium(String sodium) {
        this.sodium = sodium;
    }

    /**
     *
     * @return
     * The energy
     */
    public String getEnergy() {
        return energy;
    }

    /**
     *
     * @param energy
     * The energy
     */
    public void setEnergy(String energy) {
        this.energy = energy;
    }

    /**
     *
     * @return
     * The cholesterol
     */
    public String getCholesterol() {
        return cholesterol;
    }

    /**
     *
     * @param cholesterol
     * The cholesterol
     */
    public void setCholesterol(String cholesterol) {
        this.cholesterol = cholesterol;
    }

    /**
     *
     * @return
     * The fat
     */
    public String getFat() {
        return fat;
    }

    /**
     *
     * @param fat
     * The fat
     */
    public void setFat(String fat) {
        this.fat = fat;
    }

    /**
     *
     * @return
     * The sugar
     */
    public String getSugar() {
        return sugar;
    }

    /**
     *
     * @param sugar
     * The sugar
     */
    public void setSugar(String sugar) {
        this.sugar = sugar;
    }

    /**
     *
     * @return
     * The carbohydrate
     */
    public String getCarbohydrate() {
        return carbohydrate;
    }

    /**
     *
     * @param carbohydrate
     * The carbohydrate
     */
    public void setCarbohydrate(String carbohydrate) {
        this.carbohydrate = carbohydrate;
    }

    /**
     *
     * @return
     * The saturatedFat
     */
    public String getSaturatedFat() {
        return saturatedFat;
    }

    /**
     *
     * @param saturatedFat
     * The saturated fat
     */
    public void setSaturatedFat(String saturatedFat) {
        this.saturatedFat = saturatedFat;
    }

    /**
     *
     * @return
     * The portion
     */
    public String getPortion() {
        return portion;
    }

    /**
     *
     * @param portion
     * The portion
     */
    public void setPortion(String portion) {
        this.portion = portion;
    }

    /**
     *
     * @return
     * The monounsaturatedFat
     */
    public String getMonounsaturatedFat() {
        return monounsaturatedFat;
    }

    /**
     *
     * @param monounsaturatedFat
     * The monounsaturated fat
     */
    public void setMonounsaturatedFat(String monounsaturatedFat) {
        this.monounsaturatedFat = monounsaturatedFat;
    }

    /**
     *
     * @return
     * The protein
     */
    public String getProtein() {
        return protein;
    }

    /**
     *
     * @param protein
     * The protein
     */
    public void setProtein(String protein) {
        this.protein = protein;
    }

    /**
     *
     * @return
     * The potassium
     */
    public String getPotassium() {
        return potassium;
    }

    /**
     *
     * @param potassium
     * The potassium
     */
    public void setPotassium(String potassium) {
        this.potassium = potassium;
    }

}