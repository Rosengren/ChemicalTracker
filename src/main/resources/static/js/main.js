/* Define API endpoints once globally */
$.fn.api.settings.api = {
    'add location'    : '/api/add/location',
    'add room'        : '/api/add/room',
    'add cabinet'     : '/api/add/cabinet',
    'add chemical'    : '/api/add/chemical',
    'search'          : '/api/search/chemicals?q={query}'
};

$('.dropdown')
    .dropdown()
;

/* Following Menu Bar */
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
    })
;


$menu = $('#sidebar');
$menu.sidebar('attach events', '.view-ui');
$menu.sidebar({
    dimPage          : true,
    transition       : 'overlay',
    mobileTransition : 'uncover'
});


$('.addModal').click(function() {
    $('.ui.small.modal')
        .modal({
            closable  : false
        })
        .modal('show');
});


$('.ui.search')
    .search({
        minCharacters : 3,
        action: 'search',
        fields: {
            results     : 'chemicals',
            title       : 'name',
            description : ''
        },
        onSelect: function(chemical) {

            $.ajax({
                type        : 'post',
                dataType    : 'json',
                contentType : 'application/json',
                mimeType    : 'application/json',
                url         : location.origin + '/api/update',
                data        : JSON.stringify({
                    requestType : 'ADD',
                    location    : $('#location-name').attr('value'),
                    room        : $('#room-name').attr('value'),
                    cabinet     : $('#cabinet-name').attr('value'),
                    chemical    : chemical.name
                }),
                beforeSend: function() {
                    $(".fixedLoader").addClass('active');
                },
                success: function(response) {
                    console.log(response);
                    if (response.success) {
                        // TODO: simplify
                        var card = $('#chemicalCardTemplate').clone();
                        card.attr('id', '')
                            .appendTo('#chemicalCards')
                            .find(".header").html(chemical.name);

                        card.find(".image").click(function() {
                            window.location+='/' + chemical.name
                        });

                        card.find(".imageURL").attr("src", chemical.imageURL);
                        card.find(".remove").attr("data", name);

                        card.find(".remove").click(function() {

                            var parentCard = $(this).parents().eq(2);

                            $('.ui.basic.modal.confirm')
                                .modal({
                                    onApprove: function() {
                                        parentCard.remove();
                                        $.ajax({
                                            url: '/api/update',
                                            type: 'post',
                                            dataType: "json",
                                            contentType: "application/json",
                                            mimeType: "application/json",
                                            data: JSON.stringify({
                                                requestType : 'REMOVE',
                                                location    : $('#location-name').attr('value'),
                                                room        : $('#room-name').attr('value'),
                                                cabinet     : $('#cabinet-name').attr('value'),
                                                chemical    : chemical.name
                                            }),
                                            success: function(response) { },
                                            error: function(e) { }
                                        });
                                    }
                                })
                                .modal('show')
                            ;

                        });

                        card.show();
                        $('#noStorages').hide();
                    } else {
                        // Display message to the user
                    }
                },
                error: function(e) {
                    console.log("ERROR:");
                    console.log(e);
                },
                complete: function(e) {
                    $('.fixedLoader').removeClass('active');
                    $('#no-chemicals-msg').remove();
                }
            });
        }
    })
;

$('#addStorageForm').find('.submit').click(function() {
    var url = $('#add-url').attr('value');
    var formData = new FormData($('#addStorageForm')[0]);

    $.ajax({
        type: 'post',
        url: url,
        contentType: false,
        processData: false,
        cache: false,
        data: formData,
        beforeSend: function() {
            $('.fixedLoader').addClass('active');
        },
        success: function(storage) {
            $('#formSubmissionMsg').find('.header').text("Submission Successful");
            $('#formSubmissionMsg').find('p').text("The object was successfully created!");
            $('#formSubmissionMsg').removeClass('error');
            $('#formSubmissionMsg').addClass("success");

            var card = $('#storageCardTemplate').clone();
            card.attr('id', '')
                .appendTo('#storageCards')
                .find(".header").html(storage.name);

            card.find(".image").click(function() {
                window.location+='/' + storage.name
            });

            card.find('.description')
                .html(storage.description);

            card.find(".imageURL").attr("src", storage.imageURL);
            card.find(".remove").attr("data", storage.id);

            card.find(".remove").click(function() {
                var parentCard = $(this).parents().eq(4);
                var storageName = $(this).attr('data');
                var parentID = $('#parentID').attr('value');
                var url = $('#remove-url').attr('value');
                $('.ui.basic.modal.confirm')
                    .modal({
                        onApprove: function() {
                            $.ajax({
                                url: url + storageName + '/from/' + parentID,
                                type: 'get',
                                success: function() {
                                    parentCard.remove();
                                },
                                error: function(e) { }
                            });
                        }
                    })
                    .modal('show');
            });

            $('.dropdown')
                .dropdown()
            ;

            card.show();

            $('#noStorages').hide();
        },
        error: function() {
            $('#formSubmissionMsg .header').text("Submission Failed");
            $('#formSubmissionMsg p').text("An error occurred while creating object!");
            $('#formSubmissionMsg').addClass("error");
            $('#formSubmissionMsg').removeClass("success");
        },
        complete: function() {
            $("#noStorages").hide();
            $('#formSubmissionMsg').show();
            $('.fixedLoader').removeClass('active');
        }
    });
});

$('.storage .remove')
    .click(function() {
        var parentCard = $(this).parents().eq(4);
        var url = $('#remove-url').attr('value');
        var storageName = $(this).attr('data');
        var parentID = $('#parentID').attr('value');
        $('.ui.basic.modal.confirm')
            .modal({
                onApprove: function() {
                    $.ajax({
                        url: url + storageName + '/from/' + parentID,
                        type: 'get',
                        success: function() {
                            parentCard.remove();
                        },
                        error: function(e) { }
                    });
                }
            })
            .modal('show');
    })
;

$('.chemical .remove')
    .click(function() {
        var parentCard = $(this).parents().eq(2);
        var chemicalName = $(this).attr('data');
        $('.ui.basic.modal.confirm')
            .modal({
                onApprove: function() {
                    parentCard.remove();
                    $.ajax({
                        url: '/api/update',
                        type: 'post',
                        dataType: "json",
                        contentType: "application/json",
                        mimeType: "application/json",
                        data: JSON.stringify({
                            requestType : 'REMOVE',
                            location    : $('#location-name').attr('value'),
                            room        : $('#room-name').attr('value'),
                            cabinet     : $('#cabinet-name').attr('value'),
                            chemical    : chemicalName
                        }),
                        success: function() {
                            $('#noStorages').hide();
                        },
                        error: function(e) { }
                    });
                }
            })
            .modal('show');
    })
;

$('.button').popup({
    variation: 'inverted',
    position: 'top center'
});


$('.message .close').on('click', function() {
    $(this).closest('.message')
        .transition('fade');
});