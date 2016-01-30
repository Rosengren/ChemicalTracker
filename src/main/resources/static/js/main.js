$(document).ready(function() {
    $('#storageForm input[name="Location"]').val(window.location.pathname);
});

$(".clickableContainer").click(function() {
    window.location = $(this).find("a").attr("href"); 
    return false;
});

var selectedChemical = null;
var selectedChemicalName = "";
$(".remove").click(function() {
    selectedChemicalName = $(this).attr("data");
    selectedChemical = $(this).closest(".column");
    $('.ui.basic.modal.confirm').modal('show');
});

$("#confirmRemove").click(function() {
    // TODO: add AJAX to permanently remove from DB
    // Use selectedChemicalName in AJAX Call
    if (selectedChemical) {
        selectedChemical.remove();
        removeChemical(selectedChemicalName);
    }
    $('.ui.basic.modal.confirm').modal('hide');
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
    $('.ui.modal.addStorageModal').modal('show');
    $('.ui.modal.addChemicalModal').modal('show');
});

/** Required for making AJAX POST requests **/
$(function () {
    var token = $("#csrf").attr("value");
    var header = "X-CSRF-TOKEN";
    $(document).ajaxSend(function(e, xhr, options) {
        xhr.setRequestHeader(header, token);
    });
});

$('.ui.dropdown')
  .dropdown();

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

function removeChemical(chemicalName) {

    var username = $("#username").attr("value");
    var url = $("#removeURL").attr("value");
    console.log(JSON.stringify(chemicalName));
    console.log({"chemicalName": chemicalName});
    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        mimeType: "application/json",
        data: JSON.stringify({"chemicalName" :chemicalName}),
        success: function(response) { },
        error: function(e) { }
    });
}

$('.message .close').on('click', function() {
    $(this).closest('.message')
           .transition('fade');
});

$('#editStorage').on('click', function() {
    $('.ui.modal.editStorageModal').modal('show');
});
