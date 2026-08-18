package com.woocommerce;

import java.util.List;

/**
 * 1.1.0: holder for a paged GET response plus the pagination headers WooCommerce
 * sets on list endpoints ({@code X-WP-Total}, {@code X-WP-TotalPages}). Returned by
 * {@link WooCommerce#getAllWithTotals} and {@link WooCommerce#getAllVariationsWithTotals}.
 *
 * <p>The plain {@link WooCommerce#getAll} method is untouched by this addition -
 * it keeps returning a bare {@code List} with no header information, exactly as
 * before.
 */
public class WooPage<T> {

    private final List<T> items;
    private final int total;
    private final int totalPages;

    public WooPage(List<T> items, int total, int totalPages) {
        this.items = items;
        this.total = total;
        this.totalPages = totalPages;
    }

    /** The parsed page of entities. */
    public List<T> getItems() {
        return items;
    }

    /** Value of the X-WP-Total response header (total matching entities across all pages), or -1 if absent. */
    public int getTotal() {
        return total;
    }

    /** Value of the X-WP-TotalPages response header, or -1 if absent. */
    public int getTotalPages() {
        return totalPages;
    }
}
