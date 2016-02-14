$('.ui.search')
    .search({
        minCharacters : 3,
        apiSettings: {
            url: location.origin + '/api/search/chemicals?q={query}'
        },
        fields: {
            results     : 'chemicals',
            title       : 'name',
            description : ''
        },
        onSelect: function(chemical) {

            var request = {
                requestType : 'ADD',
                location    : $('#location-name').val(),
                room        : $('#room-name').val(),
                cabinet     : $('#cabinet-name').val(),
                chemical    : chemical
            };

            $.ajax({
                type        : 'post',
                dataType    : 'json',
                contentType : 'application/json',
                mimeType    : 'application/json',
                url         : location.origin + '/api/update',
                data: JSON.stringify(request),
                beforeSend: function() {
                    $(".fixedLoader").addClass('active');
                },
                success: function(response) {

                    if (response.success) {
                        // TODO: simplify
                        var card = $('#cardTemplate').clone();
                        card.attr('id', '')
                            .appendTo('#chemicalCards')
                            .find(".header").html(chemical.name);

                        card.find(".image").click(function() {
                            window.location+='/' + chemical.name
                        });

                        card.find(".imageURL").attr("src", chemical.imageURL);
                        card.find(".remove").attr("data", name);

                        card.find(".remove").click(function() {
                            selectedChemicalName = $(this).attr("data");
                            selectedChemical = $(this).closest(".column");
                            $('.ui.basic.modal.confirm').modal('show');
                        });

                        card.show();

                        $("#noStorages").hide();
                    } else {
                        // Display message to the user
                    }


                },
                error: function(e) {
                    console.log("ERROR:");
                    console.log(e);
                },
                complete: function(e) {
                    $(".fixedLoader").removeClass('active');
                }
            });
        }
    });


// TODO: Improve //
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
    position: 'top center'
});

$(".tooltip").popup({
    variation: 'inverted',
    position: 'top center'
});

$(".addModal").click(function() {
    $('.ui.modal.addStorageModal').modal('show');
    $('.ui.modal.searchChemicalModal').modal({
        observeChanges: true,
        onApprove : function() {
            return false;
        }
    }).modal('show');
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
            $("#noStorages").hide();
            $('#formSubmissionMsg').show();
        }
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

    console.log("REMOVING: " + chemicalName);

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
