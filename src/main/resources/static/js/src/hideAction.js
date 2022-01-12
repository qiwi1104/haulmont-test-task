function hideAction(id) {
    document.getElementById(id).remove();
}

function perform(id) {
    document.onload = hideAction(id);
}