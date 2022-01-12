function setUnderline() {
	if (document.URL.includes("clients")) {
		document.getElementById("clients").className = "underlineSelected";
	} else if (document.URL.includes("banks")) {
		document.getElementById("banks").className = "underlineSelected";
	} else if (document.URL.includes("credits")) {
		document.getElementById("credits").className = "underlineSelected";
	} else if (document.URL.includes("credit-offers")) {
	    document.getElementById("creditOffers").className = "underlineSelected";
	}
}

document.onload = setTimeout(setUnderline, 100);