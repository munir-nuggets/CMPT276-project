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

// function IsMarketOpen(){
//     const now = new Date();
//     const day = now.getDay(); // 0 for Sunday and 6 for Saturday
//     const hours = now.getHours();
//     const minutes = now.getMinutes();

//     // Check the day, time for the Market Closed....

//     // Check for Weekends 
//     if (day === 0 || day === 6){
//         return false;
//     }
//     // Check for the timing from 9:30AM to 4:00PM
//     if (hours < 9 || (hours === 9 && minutes < 30)) {
//         return false;
//     }
//     if (hours > 16 || (hours === 16 && minutes > 0)) {
//         return false;
//     }

//     return true;
// }
// function Check_Market_Status(){
//     if (!IsMarketOpen()) {
//         document.getElementById('marketClosedMessage').style.display = 'block';
//     } else {
//         document.getElementById('marketClosedMessage').style.display = 'none';
//     }
// }

// window.onload = Check_Market_Status;