const nodes = [
	{ id: "modernetraining/example-ecom-common" },
	{ id: "modernetraining/example-ecom-customer-service" },
	{ id: "modernetraining/example-ecom-fraud-detection-service" },
	{ id: "modernetraining/example-ecom-inventory-service" },
	{ id: "modernetraining/example-ecom-kyc-service" },
	{ id: "modernetraining/example-ecom-notification-service" },
	{ id: "modernetraining/example-ecom-order-service" },
	{ id: "modernetraining/example-ecom-product-service" },
	{ id: "modernetraining/example-ecom-rest-client" },
	{ id: "modernetraining/example-ecom-risk-score-service" },
	{ id: "modernetraining/example-ecom-security" }
];
const links = [
	{ source: "modernetraining/example-ecom-customer-service", target: "modernetraining/example-ecom-common", type: "dependency" },
	{ source: "modernetraining/example-ecom-customer-service", target: "modernetraining/example-ecom-security", type: "dependency" },
	{ source: "modernetraining/example-ecom-fraud-detection-service", target: "modernetraining/example-ecom-common", type: "dependency" },
	{ source: "modernetraining/example-ecom-fraud-detection-service", target: "modernetraining/example-ecom-rest-client", type: "dependency" },
	{ source: "modernetraining/example-ecom-fraud-detection-service", target: "modernetraining/example-ecom-security", type: "dependency" },
	{ source: "modernetraining/example-ecom-inventory-service", target: "modernetraining/example-ecom-common", type: "dependency" },
	{ source: "modernetraining/example-ecom-kyc-service", target: "modernetraining/example-ecom-common", type: "dependency" },
	{ source: "modernetraining/example-ecom-notification-service", target: "modernetraining/example-ecom-common", type: "dependency" },
	{ source: "modernetraining/example-ecom-order-service", target: "modernetraining/example-ecom-common", type: "dependency" },
	{ source: "modernetraining/example-ecom-order-service", target: "modernetraining/example-ecom-rest-client", type: "dependency" },
	{ source: "modernetraining/example-ecom-order-service", target: "modernetraining/example-ecom-security", type: "dependency" },
	{ source: "modernetraining/example-ecom-product-service", target: "modernetraining/example-ecom-common", type: "dependency" },
	{ source: "modernetraining/example-ecom-product-service", target: "modernetraining/example-ecom-security", type: "dependency" },
	{ source: "modernetraining/example-ecom-rest-client", target: "modernetraining/example-ecom-common", type: "dependency" },
	{ source: "modernetraining/example-ecom-rest-client", target: "modernetraining/example-ecom-security", type: "dependency" },
	{ source: "modernetraining/example-ecom-risk-score-service", target: "modernetraining/example-ecom-common", type: "dependency" },
	{ source: "modernetraining/example-ecom-security", target: "modernetraining/example-ecom-common", type: "dependency" }
];