function openForm() {
    const symbol = document.getElementById("symbol").innerText;
    const price = document.getElementById("currentPrice").innerText;
    document.getElementById("modalSymbol").value = symbol;
    document.getElementById("modalPrice").value = price;
    document.getElementById("myModal").style.display = "block";
}

function closeForm() {
    document.getElementById("myModal").style.display = "none";
}

function setPending() {
    document.getElementById("modalPrice").value = -9999.99;
}

function calculateTotal() {
    const price = parseFloat(document.getElementById("modalPrice").value);
    const quantity = parseInt(document.getElementById("quantity").value);
    const total = price * quantity;
    document.getElementById("total").value = total.toFixed(2);
}