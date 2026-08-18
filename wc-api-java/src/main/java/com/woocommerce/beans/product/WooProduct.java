
package com.woocommerce.beans.product;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.woocommerce.beans.common.Links;
import com.woocommerce.beans.common.WooBatchError;
import com.woocommerce.beans.order.MetadData;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "id",
    "name",
    "slug",
    "permalink",
    "date_created",
    "date_modified",
    "type",
    "status",
    "featured",
    "catalog_visibility",
    "description",
    "short_description",
    "sku",
    "price",
    "regular_price",
    "sale_price",
    "date_on_sale_from",
    "date_on_sale_from_gmt",
    "date_on_sale_to",
    "date_on_sale_to_gmt",
    "price_html",
    "on_sale",
    "purchasable",
    "total_sales",
    "virtual",
    "downloadable",
    "downloads",
    "download_limit",
    "download_expiry",
    "download_type",
    "external_url",
    "button_text",
    "tax_status",
    "tax_class",
    "manage_stock",
    "stock_quantity",
    "stock_status",
    "in_stock",
    "backorders",
    "backorders_allowed",
    "backordered",
    "sold_individually",
    "weight",
    "dimensions",
    "shipping_required",
    "shipping_taxable",
    "shipping_class",
    "shipping_class_id",
    "reviews_allowed",
    "average_rating",
    "rating_count",
    "related_ids",
    "upsell_ids",
    "cross_sell_ids",
    "parent_id",
    "purchase_note",
    "categories",
    "tags",
    "images",
    "attributes",
    "default_attributes",
    "variations",
    "grouped_products",
    "menu_order",
    "meta_data",
    "_links",
    "error"
})
public class WooProduct {

    @JsonProperty("id")
    private Long id;
    @JsonProperty("name")
    private String name;
    @JsonProperty("slug")
    private String slug;
    @JsonProperty("permalink")
    private String permalink;
    @JsonProperty("date_created")
    private String dateCreated;
    @JsonProperty("date_modified")
    private String dateModified;
    @JsonProperty("type")
    private String type;
    @JsonProperty("status")
    private String status;
    @JsonProperty("featured")
    private Boolean featured;
    @JsonProperty("catalog_visibility")
    private String catalogVisibility;
    @JsonProperty("description")
    private String description;
    @JsonProperty("short_description")
    private String shortDescription;
    @JsonProperty("sku")
    private String sku;
    @JsonProperty("price")
    private String price;
    @JsonProperty("regular_price")
    private String regularPrice;
    @JsonProperty("sale_price")
    private String salePrice;
    @JsonProperty("date_on_sale_from")
    private String dateOnSaleFrom;
    @JsonProperty("date_on_sale_from_gmt")
    private String dateOnSaleFromGmt;
    @JsonProperty("date_on_sale_to")
    private String dateOnSaleTo;
    @JsonProperty("date_on_sale_to_gmt")
    private String dateOnSaleToGmt;
    @JsonProperty("price_html")
    private String priceHtml;
    @JsonProperty("on_sale")
    private Boolean onSale;
    @JsonProperty("purchasable")
    private Boolean purchasable;
    @JsonProperty("total_sales")
    private Long totalSales;
    @JsonProperty("virtual")
    private Boolean virtual;
    @JsonProperty("downloadable")
    private Boolean downloadable;
    @JsonProperty("downloads")
    private List<Object> downloads = null;
    @JsonProperty("download_limit")
    private Long downloadLimit;
    @JsonProperty("download_expiry")
    private Long downloadExpiry;
    @JsonProperty("download_type")
    private String downloadType;
    @JsonProperty("external_url")
    private String externalUrl;
    @JsonProperty("button_text")
    private String buttonText;
    @JsonProperty("tax_status")
    private String taxStatus;
    @JsonProperty("tax_class")
    private String taxClass;
    @JsonProperty("manage_stock")
    private Boolean manageStock;
    @JsonProperty("stock_quantity")
    private Object stockQuantity;
    @JsonProperty("stock_status")
    private String stockStatus;
    @JsonProperty("in_stock")
    private Boolean inStock;
    @JsonProperty("backorders")
    private String backorders;
    @JsonProperty("backorders_allowed")
    private Boolean backordersAllowed;
    @JsonProperty("backordered")
    private Boolean backordered;
    @JsonProperty("sold_individually")
    private Boolean soldIndividually;
    @JsonProperty("weight")
    private String weight;
    @JsonProperty("dimensions")
    private Dimensions dimensions;
    @JsonProperty("shipping_required")
    private Boolean shippingRequired;
    @JsonProperty("shipping_taxable")
    private Boolean shippingTaxable;
    @JsonProperty("shipping_class")
    private String shippingClass;
    @JsonProperty("shipping_class_id")
    private Long shippingClassId;
    @JsonProperty("reviews_allowed")
    private Boolean reviewsAllowed;
    @JsonProperty("average_rating")
    private String averageRating;
    @JsonProperty("rating_count")
    private Long ratingCount;
    @JsonProperty("related_ids")
    private List<Long> relatedIds = null;
    @JsonProperty("upsell_ids")
    private List<Object> upsellIds = null;
    @JsonProperty("cross_sell_ids")
    private List<Object> crossSellIds = null;
    @JsonProperty("parent_id")
    private Long parentId;
    @JsonProperty("purchase_note")
    private String purchaseNote;
    @JsonProperty("categories")
    private List<Category> categories = null;
    @JsonProperty("tags")
    private List<Object> tags = null;
    @JsonProperty("images")
    private List<Image> images = null;
    @JsonProperty("attributes")
    private List<Object> attributes = null;
    @JsonProperty("default_attributes")
    private List<Object> defaultAttributes = null;
    @JsonProperty("variations")
    private List<Object> variations = null;
    @JsonProperty("grouped_products")
    private List<Object> groupedProducts = null;
    @JsonProperty("menu_order")
    private Long menuOrder;
    @JsonProperty("meta_data")
    private List<MetadData> metaData = null;
    @JsonProperty("_links")
    private Links links;
    // 1.1.0: products/batch and products/{id}/variations/batch never fail the whole
    // request when one item is invalid - the failed item comes back with its normal
    // fields mostly empty and this "error" object populated instead. Null on every
    // other response (create/update/get), so JsonInclude.NON_NULL keeps it out of
    // request bodies we send.
    @JsonProperty("error")
    private WooBatchError error;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public Long getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(Long id) {
        this.id = id;
    }

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("slug")
    public String getSlug() {
        return slug;
    }

    @JsonProperty("slug")
    public void setSlug(String slug) {
        this.slug = slug;
    }

    @JsonProperty("permalink")
    public String getPermalink() {
        return permalink;
    }

    @JsonProperty("permalink")
    public void setPermalink(String permalink) {
        this.permalink = permalink;
    }

    @JsonProperty("date_created")
    public String getDateCreated() {
        return dateCreated;
    }

    @JsonProperty("date_created")
    public void setDateCreated(String dateCreated) {
        this.dateCreated = dateCreated;
    }

    @JsonProperty("date_modified")
    public String getDateModified() {
        return dateModified;
    }

    @JsonProperty("date_modified")
    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("featured")
    public Boolean getFeatured() {
        return featured;
    }

    @JsonProperty("featured")
    public void setFeatured(Boolean featured) {
        this.featured = featured;
    }

    @JsonProperty("catalog_visibility")
    public String getCatalogVisibility() {
        return catalogVisibility;
    }

    @JsonProperty("catalog_visibility")
    public void setCatalogVisibility(String catalogVisibility) {
        this.catalogVisibility = catalogVisibility;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("short_description")
    public String getShortDescription() {
        return shortDescription;
    }

    @JsonProperty("short_description")
    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    @JsonProperty("sku")
    public String getSku() {
        return sku;
    }

    @JsonProperty("sku")
    public void setSku(String sku) {
        this.sku = sku;
    }

    @JsonProperty("price")
    public String getPrice() {
        return price;
    }

    @JsonProperty("price")
    public void setPrice(String price) {
        this.price = price;
    }

    @JsonProperty("regular_price")
    public String getRegularPrice() {
        return regularPrice;
    }

    @JsonProperty("regular_price")
    public void setRegularPrice(String regularPrice) {
        this.regularPrice = regularPrice;
    }

    @JsonProperty("sale_price")
    public String getSalePrice() {
        return salePrice;
    }

    @JsonProperty("sale_price")
    public void setSalePrice(String salePrice) {
        this.salePrice = salePrice;
    }

    @JsonProperty("date_on_sale_from")
    public String getDateOnSaleFrom() {
        return dateOnSaleFrom;
    }

    @JsonProperty("date_on_sale_from")
    public void setDateOnSaleFrom(String dateOnSaleFrom) {
        this.dateOnSaleFrom = dateOnSaleFrom;
    }

    @JsonProperty("date_on_sale_from_gmt")
    public String getDateOnSaleFromGmt() {
        return dateOnSaleFromGmt;
    }

    @JsonProperty("date_on_sale_from_gmt")
    public void setDateOnSaleFromGmt(String dateOnSaleFromGmt) {
        this.dateOnSaleFromGmt = dateOnSaleFromGmt;
    }

    @JsonProperty("date_on_sale_to")
    public String getDateOnSaleTo() {
        return dateOnSaleTo;
    }

    @JsonProperty("date_on_sale_to")
    public void setDateOnSaleTo(String dateOnSaleTo) {
        this.dateOnSaleTo = dateOnSaleTo;
    }

    @JsonProperty("date_on_sale_to_gmt")
    public String getDateOnSaleToGmt() {
        return dateOnSaleToGmt;
    }

    @JsonProperty("date_on_sale_to_gmt")
    public void setDateOnSaleToGmt(String dateOnSaleToGmt) {
        this.dateOnSaleToGmt = dateOnSaleToGmt;
    }

    @JsonProperty("price_html")
    public String getPriceHtml() {
        return priceHtml;
    }

    @JsonProperty("price_html")
    public void setPriceHtml(String priceHtml) {
        this.priceHtml = priceHtml;
    }

    @JsonProperty("on_sale")
    public Boolean getOnSale() {
        return onSale;
    }

    @JsonProperty("on_sale")
    public void setOnSale(Boolean onSale) {
        this.onSale = onSale;
    }

    @JsonProperty("purchasable")
    public Boolean getPurchasable() {
        return purchasable;
    }

    @JsonProperty("purchasable")
    public void setPurchasable(Boolean purchasable) {
        this.purchasable = purchasable;
    }

    @JsonProperty("total_sales")
    public Long getTotalSales() {
        return totalSales;
    }

    @JsonProperty("total_sales")
    public void setTotalSales(Long totalSales) {
        this.totalSales = totalSales;
    }

    @JsonProperty("virtual")
    public Boolean getVirtual() {
        return virtual;
    }

    @JsonProperty("virtual")
    public void setVirtual(Boolean virtual) {
        this.virtual = virtual;
    }

    @JsonProperty("downloadable")
    public Boolean getDownloadable() {
        return downloadable;
    }

    @JsonProperty("downloadable")
    public void setDownloadable(Boolean downloadable) {
        this.downloadable = downloadable;
    }

    @JsonProperty("downloads")
    public List<Object> getDownloads() {
        return downloads;
    }

    @JsonProperty("downloads")
    public void setDownloads(List<Object> downloads) {
        this.downloads = downloads;
    }

    @JsonProperty("download_limit")
    public Long getDownloadLimit() {
        return downloadLimit;
    }

    @JsonProperty("download_limit")
    public void setDownloadLimit(Long downloadLimit) {
        this.downloadLimit = downloadLimit;
    }

    @JsonProperty("download_expiry")
    public Long getDownloadExpiry() {
        return downloadExpiry;
    }

    @JsonProperty("download_expiry")
    public void setDownloadExpiry(Long downloadExpiry) {
        this.downloadExpiry = downloadExpiry;
    }

    @JsonProperty("download_type")
    public String getDownloadType() {
        return downloadType;
    }

    @JsonProperty("download_type")
    public void setDownloadType(String downloadType) {
        this.downloadType = downloadType;
    }

    @JsonProperty("external_url")
    public String getExternalUrl() {
        return externalUrl;
    }

    @JsonProperty("external_url")
    public void setExternalUrl(String externalUrl) {
        this.externalUrl = externalUrl;
    }

    @JsonProperty("button_text")
    public String getButtonText() {
        return buttonText;
    }

    @JsonProperty("button_text")
    public void setButtonText(String buttonText) {
        this.buttonText = buttonText;
    }

    @JsonProperty("tax_status")
    public String getTaxStatus() {
        return taxStatus;
    }

    @JsonProperty("tax_status")
    public void setTaxStatus(String taxStatus) {
        this.taxStatus = taxStatus;
    }

    @JsonProperty("tax_class")
    public String getTaxClass() {
        return taxClass;
    }

    @JsonProperty("tax_class")
    public void setTaxClass(String taxClass) {
        this.taxClass = taxClass;
    }

    @JsonProperty("manage_stock")
    public Boolean getManageStock() {
        return manageStock;
    }

    @JsonProperty("manage_stock")
    public void setManageStock(Boolean manageStock) {
        this.manageStock = manageStock;
    }

    @JsonProperty("stock_quantity")
    public Object getStockQuantity() {
        return stockQuantity;
    }

    @JsonProperty("stock_quantity")
    public void setStockQuantity(Object stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    @JsonProperty("stock_status")
    public String getStockStatus() {
        return stockStatus;
    }

    @JsonProperty("stock_status")
    public void setStockStatus(String stockStatus) {
        this.stockStatus = stockStatus;
    }

    @JsonProperty("in_stock")
    public Boolean getInStock() {
        return inStock;
    }

    @JsonProperty("in_stock")
    public void setInStock(Boolean inStock) {
        this.inStock = inStock;
    }

    @JsonProperty("backorders")
    public String getBackorders() {
        return backorders;
    }

    @JsonProperty("backorders")
    public void setBackorders(String backorders) {
        this.backorders = backorders;
    }

    @JsonProperty("backorders_allowed")
    public Boolean getBackordersAllowed() {
        return backordersAllowed;
    }

    @JsonProperty("backorders_allowed")
    public void setBackordersAllowed(Boolean backordersAllowed) {
        this.backordersAllowed = backordersAllowed;
    }

    @JsonProperty("backordered")
    public Boolean getBackordered() {
        return backordered;
    }

    @JsonProperty("backordered")
    public void setBackordered(Boolean backordered) {
        this.backordered = backordered;
    }

    @JsonProperty("sold_individually")
    public Boolean getSoldIndividually() {
        return soldIndividually;
    }

    @JsonProperty("sold_individually")
    public void setSoldIndividually(Boolean soldIndividually) {
        this.soldIndividually = soldIndividually;
    }

    @JsonProperty("weight")
    public String getWeight() {
        return weight;
    }

    @JsonProperty("weight")
    public void setWeight(String weight) {
        this.weight = weight;
    }

    @JsonProperty("dimensions")
    public Dimensions getDimensions() {
        return dimensions;
    }

    @JsonProperty("dimensions")
    public void setDimensions(Dimensions dimensions) {
        this.dimensions = dimensions;
    }

    @JsonProperty("shipping_required")
    public Boolean getShippingRequired() {
        return shippingRequired;
    }

    @JsonProperty("shipping_required")
    public void setShippingRequired(Boolean shippingRequired) {
        this.shippingRequired = shippingRequired;
    }

    @JsonProperty("shipping_taxable")
    public Boolean getShippingTaxable() {
        return shippingTaxable;
    }

    @JsonProperty("shipping_taxable")
    public void setShippingTaxable(Boolean shippingTaxable) {
        this.shippingTaxable = shippingTaxable;
    }

    @JsonProperty("shipping_class")
    public String getShippingClass() {
        return shippingClass;
    }

    @JsonProperty("shipping_class")
    public void setShippingClass(String shippingClass) {
        this.shippingClass = shippingClass;
    }

    @JsonProperty("shipping_class_id")
    public Long getShippingClassId() {
        return shippingClassId;
    }

    @JsonProperty("shipping_class_id")
    public void setShippingClassId(Long shippingClassId) {
        this.shippingClassId = shippingClassId;
    }

    @JsonProperty("reviews_allowed")
    public Boolean getReviewsAllowed() {
        return reviewsAllowed;
    }

    @JsonProperty("reviews_allowed")
    public void setReviewsAllowed(Boolean reviewsAllowed) {
        this.reviewsAllowed = reviewsAllowed;
    }

    @JsonProperty("average_rating")
    public String getAverageRating() {
        return averageRating;
    }

    @JsonProperty("average_rating")
    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    @JsonProperty("rating_count")
    public Long getRatingCount() {
        return ratingCount;
    }

    @JsonProperty("rating_count")
    public void setRatingCount(Long ratingCount) {
        this.ratingCount = ratingCount;
    }

    @JsonProperty("related_ids")
    public List<Long> getRelatedIds() {
        return relatedIds;
    }

    @JsonProperty("related_ids")
    public void setRelatedIds(List<Long> relatedIds) {
        this.relatedIds = relatedIds;
    }

    @JsonProperty("upsell_ids")
    public List<Object> getUpsellIds() {
        return upsellIds;
    }

    @JsonProperty("upsell_ids")
    public void setUpsellIds(List<Object> upsellIds) {
        this.upsellIds = upsellIds;
    }

    @JsonProperty("cross_sell_ids")
    public List<Object> getCrossSellIds() {
        return crossSellIds;
    }

    @JsonProperty("cross_sell_ids")
    public void setCrossSellIds(List<Object> crossSellIds) {
        this.crossSellIds = crossSellIds;
    }

    @JsonProperty("parent_id")
    public Long getParentId() {
        return parentId;
    }

    @JsonProperty("parent_id")
    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @JsonProperty("purchase_note")
    public String getPurchaseNote() {
        return purchaseNote;
    }

    @JsonProperty("purchase_note")
    public void setPurchaseNote(String purchaseNote) {
        this.purchaseNote = purchaseNote;
    }

    @JsonProperty("categories")
    public List<Category> getCategories() {
        return categories;
    }

    @JsonProperty("categories")
    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @JsonProperty("tags")
    public List<Object> getTags() {
        return tags;
    }

    @JsonProperty("tags")
    public void setTags(List<Object> tags) {
        this.tags = tags;
    }

    @JsonProperty("images")
    public List<Image> getImages() {
        return images;
    }

    @JsonProperty("images")
    public void setImages(List<Image> images) {
        this.images = images;
    }

    @JsonProperty("attributes")
    public List<Object> getAttributes() {
        return attributes;
    }

    @JsonProperty("attributes")
    public void setAttributes(List<Object> attributes) {
        this.attributes = attributes;
    }

    @JsonProperty("default_attributes")
    public List<Object> getDefaultAttributes() {
        return defaultAttributes;
    }

    @JsonProperty("default_attributes")
    public void setDefaultAttributes(List<Object> defaultAttributes) {
        this.defaultAttributes = defaultAttributes;
    }

    @JsonProperty("variations")
    public List<Object> getVariations() {
        return variations;
    }

    @JsonProperty("variations")
    public void setVariations(List<Object> variations) {
        this.variations = variations;
    }

    @JsonProperty("grouped_products")
    public List<Object> getGroupedProducts() {
        return groupedProducts;
    }

    @JsonProperty("grouped_products")
    public void setGroupedProducts(List<Object> groupedProducts) {
        this.groupedProducts = groupedProducts;
    }

    @JsonProperty("menu_order")
    public Long getMenuOrder() {
        return menuOrder;
    }

    @JsonProperty("menu_order")
    public void setMenuOrder(Long menuOrder) {
        this.menuOrder = menuOrder;
    }

    @JsonProperty("meta_data")
    public List<MetadData> getMetaData() {
        return metaData;
    }

    @JsonProperty("meta_data")
    public void setMetaData(List<MetadData> metaData) {
        this.metaData = metaData;
    }

    @JsonProperty("_links")
    public Links getLinks() {
        return links;
    }

    @JsonProperty("_links")
    public void setLinks(Links links) {
        this.links = links;
    }

    @JsonProperty("error")
    public WooBatchError getError() {
        return error;
    }

    @JsonProperty("error")
    public void setError(WooBatchError error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
