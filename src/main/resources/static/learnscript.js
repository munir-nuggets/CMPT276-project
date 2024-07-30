let contentData = {};
let contents;

document.addEventListener("DOMContentLoaded", function() {
    fetch('learningArticles/basics.txt')
        .then(response => response.text())
        .then(data => {
            contents = data;
        })
        .catch(error => console.error('Error loading txt data:', error));
});

// Fetch JSON data when the page loads
document.addEventListener("DOMContentLoaded", function() {
    fetch('content.json')
        .then(response => response.json())
        .then(data => {
            contentData = data;
        })
        .catch(error => console.error('Error loading JSON data:', error));
});

function loadContent(contentKey) {
    const content = contentData[contentKey];
    console.log(content);
    if (content) {
        const mainContent = document.getElementById('main-content');
        let bodyContent = '<ul>';
        content.forEach(item => {
            console.log(item);
            bodyContent += `<li><a href="${item}">${item}<a></li>`;
        });
        bodyContent += '</ul>';
        mainContent.innerHTML = `<h2>${contentKey}</h2>${bodyContent}`;
        //mainContent.innerHTML = `<p> ${contents} </p>`;
    } else {
        console.error('Content not found for key:', contentKey);
    }
}

    