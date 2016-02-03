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
    $('.ui.modal.searchChemicalModal').modal({
        onApprove : function() {
            return false;
        }
    }).modal('show');
});

// $('.addChemicalModal.modal')
//   .modal('attach events', '.ui.modal.searchChemicalModal');

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

$("#submitChemicalSearch").click(function() {

    var url = $("#searchChemicalURL").attr("value");
    var request = {
        "chemical" : $("#chemicalQuery").val()
    };

    $results = $("#chemicalSearchResults");
    $results.empty();
    $.ajax({
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        mimeType: "application/json",
        url: url,
        data: JSON.stringify(request),
        success: function(response) {
            console.log(response);

            if (response.match) {
                response.chemicalNames.forEach(function(elem) {
                    $('<a/>', {
                        text  : elem,
                        class : 'item',
                        click : function() {
                            addChemical($(this).html());
                        }
                    }).appendTo($results);
                });
            } else {
                $results.append("No Matches Found!");
            }
        },
        error: function(response) {
            $results.append("Invalid Query");
            console.log("ERROR:");
            console.log(response);
        }
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

function addChemical(name) {

    var username = $("#username").attr("value");
    var url = $("#addURL").attr("value");

    var selectedChemicals = [];
    selectedChemicals.push(name);

    $.ajax({
        url: url,
        type: "POST",
        dataType: "json",
        contentType: "application/json",
        mimeType: "application/json",
        data: JSON.stringify(selectedChemicals),
        success: function(response) {
            console.log(response);
            var card = $("#cardTemplate").clone();
            card.attr("id", "")
                .appendTo("#chemicalCards")
                .find(".header").html(name);

            card.find(".description").html("Description Here");
            card.find(".image").click(function() { window.location+='/' + name});
            card.find(".imageURL").attr("src", response.imageURL);
            card.show();
        },
        error: function(e) {
            console.log("ERROR:");
            console.log(response);
        }
    });
}

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

$('body')
      .visibility({
        offset         : -10,
        observeChanges : false,
        once           : false,
        continuous     : false,
        onTopPassed: function() {
          requestAnimationFrame(function() {
            $('.following.bar')
              .addClass('light fixed')
              .find('.menu')
                .removeClass('inverted')
            ;
            $('.following .additional.item')
              .transition('scale in', 750)
            ;
          });
        },
        onTopPassedReverse: function() {
          requestAnimationFrame(function() {
            $('.following.bar')
              .removeClass('light fixed')
              .find('.menu')
                .addClass('inverted')
                .find('.additional.item')
                  .transition('hide')
            ;
          });
        }
      });

$menu = $('#sidebar');
$menu.sidebar('attach events', '.view-ui');
$menu.sidebar({
      dimPage          : true,
      transition       : 'overlay',
      mobileTransition : 'uncover'
    });
