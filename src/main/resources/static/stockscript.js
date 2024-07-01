function filterStocks() {
    var input, filter, ul, li, a, i, txtValue;
    input = document.getElementById('searchInput');
    filter = input.value.toUpperCase();
    ul = document.getElementById("stockList");
    li = ul.getElementsByTagName('li');

    if (filter.trim() === '') {
        ul.style.display = 'none';
        ul.style.height = '0'; 
        return;
    }

    ul.style.display = 'block';

    var visibleItemCount = 0;
    for (i = 0; i < li.length; i++) {
        a = li[i].getElementsByTagName("a")[0];
        txtValue = a.textContent || a.innerText;
        if (txtValue.toUpperCase().indexOf(filter) > -1) {
            li[i].style.display = "";
            visibleItemCount++;
        } else {
            li[i].style.display = "none";
        }
    }

    var itemHeight = li[0].offsetHeight; 
    ul.style.height = (visibleItemCount * itemHeight) + 'px';
}
