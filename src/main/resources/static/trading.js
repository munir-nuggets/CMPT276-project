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

function confirmTrade(event) {
    event.preventDefault();
    const total = parseFloat(document.getElementById("total").value);
    const balance = parseFloat(document.getElementById("balance").innerText);
    const quantity = parseInt(document.getElementById("quantity").value);
    const quantityOwned = /*[[${quantityOwned}]]*/ 0;
    const isPurchase = document.getElementById("buy").checked;

    if (isPurchase) {
        if (total > balance) {
            alert("Insufficient funds");
            return false;
        }
    } else {
        if (quantity > quantityOwned) {
            alert("Insufficient shares");
            return false;
        }
    }

    const confirmation = confirm("Confirm trade?");
    if (confirmation) {
        document.getElementById("buyForm").submit();
    }
}