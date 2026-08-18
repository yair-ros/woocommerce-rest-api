SHELL := /bin/sh

WC_ENV_FILE ?= .woocommerce-test.env
WC_TEST ?=

.PHONY: help woocommerce-test-env woocommerce-test

help:
	@echo "Available targets:"
	@echo "  make woocommerce-test-env"
	@echo "      Create $(WC_ENV_FILE) from the committed example."
	@echo "  make woocommerce-test [WC_TEST=testMethod]"
	@echo "      Run all manual WooCommerce tests, or one selected method."

woocommerce-test-env:
	@if [ -e "$(WC_ENV_FILE)" ]; then \
		echo "$(WC_ENV_FILE) already exists; leaving it unchanged."; \
	else \
		cp .woocommerce-test.env.example "$(WC_ENV_FILE)"; \
		chmod 600 "$(WC_ENV_FILE)"; \
		echo "Created $(WC_ENV_FILE). Replace its placeholder values before running a test."; \
	fi

woocommerce-test:
	@test -f "$(WC_ENV_FILE)" || { \
		echo "Missing $(WC_ENV_FILE). Run 'make woocommerce-test-env' first." >&2; \
		exit 1; \
	}
	@set -a; \
	. "./$(WC_ENV_FILE)"; \
	set +a; \
	test_selector="WooCommerceClientTest"; \
	if [ -n "$(WC_TEST)" ]; then test_selector="$${test_selector}#$(WC_TEST)"; fi; \
	WC_RUN_MANUAL_TESTS=true mvn -f wc-api-java/pom.xml \
		-Dtest="$${test_selector}" test
