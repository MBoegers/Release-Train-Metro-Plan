const nodes = [
{ id: "mtthwcmpbll/example-ecom-customer-service" },
	{ id: "mtthwcmpbll/example-ecom-common" },
	{ id: "mtthwcmpbll/example-ecom-security" },
	{ id: "mtthwcmpbll/example-ecom-fraud-detection-service" },
	{ id: "mtthwcmpbll/example-ecom-rest-client" },
	{ id: "mtthwcmpbll/example-ecom-inventory-service" },
	{ id: "mtthwcmpbll/example-ecom-kyc-service" },
	{ id: "mtthwcmpbll/example-ecom-notification-service" },
	{ id: "mtthwcmpbll/example-ecom-order-service" },
	{ id: "mtthwcmpbll/example-ecom-product-service" },
	{ id: "mtthwcmpbll/example-ecom-risk-score-service" }
];
const links = [
{ source: "mtthwcmpbll/example-ecom-customer-service", target: "mtthwcmpbll/example-ecom-common", type: "dependency" },
	{ source: "mtthwcmpbll/example-ecom-customer-service", target: "mtthwcmpbll/example-ecom-security", type: "dependency" },
	{ source: "mtthwcmpbll/example-ecom-fraud-detection-service", target: "mtthwcmpbll/example-ecom-security", type: "dependency" },
	{ source: "mtthwcmpbll/example-ecom-fraud-detection-service", target: "mtthwcmpbll/example-ecom-rest-client", type: "dependency" },
	{ source: "mtthwcmpbll/example-ecom-fraud-detection-service", target: "mtthwcmpbll/example-ecom-common", type: "dependency" },
	{ source: "mtthwcmpbll/example-ecom-inventory-service", target: "mtthwcmpbll/example-ecom-common", type: "dependency" },
	{ source: "mtthwcmpbll/example-ecom-kyc-service", target: "mtthwcmpbll/example-ecom-common", type: "dependency" },
	{ source: "mtthwcmpbll/example-ecom-notification-service", target: "mtthwcmpbll/example-ecom-common", type: "dependency" },
	{ source: "mtthwcmpbll/example-ecom-order-service", target: "mtthwcmpbll/example-ecom-security", type: "dependency" },
	{ source: "mtthwcmpbll/example-ecom-order-service", target: "mtthwcmpbll/example-ecom-rest-client", type: "dependency" },
	{ source: "mtthwcmpbll/example-ecom-product-service", target: "mtthwcmpbll/example-ecom-common", type: "dependency" },
	{ source: "mtthwcmpbll/example-ecom-product-service", target: "mtthwcmpbll/example-ecom-security", type: "dependency" },
	{ source: "mtthwcmpbll/example-ecom-rest-client", target: "mtthwcmpbll/example-ecom-security", type: "dependency" },
	{ source: "mtthwcmpbll/example-ecom-risk-score-service", target: "mtthwcmpbll/example-ecom-common", type: "dependency" },
	{ source: "mtthwcmpbll/example-ecom-security", target: "mtthwcmpbll/example-ecom-common", type: "dependency" }
];