$(".clickableContainer").click(function() {
    window.location = $(this).find("a").attr("href"); 
    return false;
});

$(".button").popup({
    variation: 'inverted',
    position: 'top center',
});

$(".tooltip").popup({
    variation: 'inverted',
    position: 'top center',
});

$(".addModal").click(function() {
    $('.ui.modal').modal('show');
});

/** Required for making AJAX POST requests **/
$(function () {
    var token = $("#csrf").attr("value");
    var header = "X-CSRF-TOKEN";
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});

$("#submitCreateStorage").click(function() {

    var username = $("#username").attr("value");
    var url = $("#addURL").attr("value");

    var newStorageName = $("#newStorageName").val();
    var newStorageDesc = $("#newStorageDesc").val();

    if (newStorageName === null || newStorageName === "") {
        alert("missing name");
        return;
    } else if (newStorageDesc === null || newStorageDesc === "") {
        alert("missing description");
        return;
    }

    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        mimeType: "application/json",
        data: JSON.stringify({
            username: username,
            name: newStorageName,
            description: newStorageDesc
        }),
        success: function(response) {
            alert("Success!");
        },
        error: function(e) {
            console.log("Error:");
            console.log(e);
        }
    });
})
