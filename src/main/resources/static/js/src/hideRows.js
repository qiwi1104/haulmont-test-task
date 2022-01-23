function hideRows() {
    let elements = document.getElementsByClassName("hidePayments");
    let button = document.getElementById("button");

    for (let i = 0; i < elements.length; i++) {
        if (elements[i].hidden == false) {
            elements[i].hidden = true;
            button.innerText = "Show Payments";
        } else {
            elements[i].hidden = false;
            button.innerText = "Hide Payments";
        }
    }
}