$(document).ready(function() {
    $('#storageForm input[name="Location"]').val(window.location.pathname);
});

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

    var formData = new FormData($('#storageForm')[0]);
    var url = $("#addURL").attr("value");

    $.ajax({
        type: "POST",
        url: url,
        contentType: false,
        processData: false,
        cache: false,
        data: formData,
        xhr: function() { // custom XMLHttpRequest
            var myXhr = $.ajaxSettings.xhr();
            if (myXhr.upload) {
                myXhr.upload.addEventListener('progress', progressHandlingFunction, false);
            }
            return myXhr;
        },
        success: function() {
            $('#formSubmissionMsg .header').text("Submission Successful");
            $('#formSubmissionMsg p').text("The location was successfully created!");
            $('#formSubmissionMsg').removeClass("error");
            $('#formSubmissionMsg').addClass("success");
        },
        error: function() {
            $('#formSubmissionMsg .header').text("Submission Failed");
            $('#formSubmissionMsg p').text("An error occurred while creating the location!");
            $('#formSubmissionMsg').addClass("error");
            $('#formSubmissionMsg').removeClass("success");
        },
        complete: function() {
            $('#formSubmissionMsg').show();
        }
    });
});

$("#submitAddChemicalsToCabinet").click(function() {

    var username = $("#username").attr("value");
    var url = $("#addURL").attr("value");

    var selectedChemicals = [];

    var inputs = document.querySelectorAll("input[type='checkbox']");
    for(var i = 0; i < inputs.length; i++) {
        if (inputs[i].checked == true) {
            selectedChemicals.push(inputs[i].name);
        }
    }

    if (selectedChemicals.length == 0) {
        alert("No chemicals selected");
        return;
    }

    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        mimeType: "application/json",
        data: JSON.stringify(selectedChemicals),
        success: function(response) { },
        error: function(e) { }
    });
});

function progressHandlingFunction(e) {
    if (e.lengthComputable) {
        $('progress').attr({
            value: e.loaded,
            max: e.total
        });
    }
}

$('.message .close').on('click', function() {
    $(this).closest('.message')
           .transition('fade');
});
