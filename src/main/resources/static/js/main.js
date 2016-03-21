/* Define API endpoints once globally */
$(document).ready(function() {
    $.fn.api.settings.api = {
        'add storage'   : $('#add-url').attr('value'),
        'search'        : $('#es-endpoint').attr('value') + 'chemicals/chemical/_search?q=Name:*{query}*',
        'compare'       : location.href.split('?')[0] + '/compare/{oldVersion}/with/{newVersion}'
    };

    $('.dropdown')
        .dropdown()
    ;

    $('.version.dropdown')
        .dropdown({
            on: 'click',
            action: function(version) {
                location.href = window.location.href.split('?')[0] + '?v=' + version ;
            }
        })
    ;

    $('.firstVersion.dropdown')
        .dropdown()
    ;

    $('.secondVersion.dropdown')
        .dropdown()
    ;

    $('.viewChemical').click(function() {
        location.href = window.location.href.split('?')[0] + '/' + $(this).attr('data') ;
    });

    $('.editChemical').click(function() {
        var that = $(this);
        $('.ui.small.modal.updateChemicalImage')
            .modal({
                onShow : function() {
                    var chemicalName = that.attr('data');
                    $(this).find('.header').text('Update Image for ' + chemicalName);
                    $('#updateChemicalImageName').attr('value', chemicalName);
                }
            })
            .modal('show')
        ;
    });

    $('.editLocation').click(function() {
        var that = $(this);
        $('.ui.small.modal.updateLocationImage')
            .modal({
                onShow : function() {
                    var locationName = that.attr('data');
                    $(this).find('.header').text('Update Image for ' + locationName);
                    $('#updateLocationImageName').attr('value', locationName);
                }
            })
            .modal('show')
        ;
    });

    $('.editRoom').click(function() {
        var that = $(this);
        $('.ui.small.modal.updateRoomImage')
            .modal({
                onShow : function() {
                    var roomName = that.attr('data');
                    $(this).find('.header').text('Update Image for ' + roomName);
                    $('#updateRoomImageName').attr('value', roomName);
                }
            })
            .modal('show')
        ;
    });

    $('.editCabinet').click(function() {
        console.log("EDIT CABINET CLICKED");
        var that = $(this);
        $('.ui.small.modal.updateCabinetImage')
            .modal({
                onShow : function() {
                    var cabinetName = that.attr('data');
                    $(this).find('.header').text('Update Image for ' + cabinetName);
                    $('#updateCabinetImageName').attr('value', cabinetName);
                }
            })
            .modal('show')
        ;
    });

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

    $('#newVersion').click(function() {
        $('.ui.small.modal.fork')
            .modal('show');
    });

    $('#compareVersions').click(function() {
        $('.ui.small.modal.compare')
            .modal('show');
    });

    $('#compareVersionForm')
        .form({
            on: 'change',
            fields : {
                firstVersion : {
                    identifier : 'firstVersion',
                    rules: [
                        {
                            type    : 'empty',
                            prompt  : 'Please select a new version to compare.'
                        }
                    ]
                },
                secondVersion : {
                    identifier : 'secondVersion',
                    rules : [
                        {
                            type    : 'different[firstVersion]',
                            prompt  : 'Please select two different versions.'
                        },
                        {
                            type    : 'empty',
                            prompt  : 'Please select an old version to compare.'

                        }
                    ]
                }
            }
        }).api({
            action : 'compare',
            beforeSend : function(settings) {
                settings.urlData = {
                    oldVersion : $('#firstVersion').val(),
                    newVersion : $('#secondVersion').val()
                };
                return settings;
            },
            onSuccess : function(response) {

                var added = '';
                response.added.forEach(function(elem) {
                    added += '<li>' + elem +  '</li>';
                });

                var removed = '';
                response.removed.forEach(function(elem) {
                    removed += '<li>' + elem + '</li>';
                });

                var matching = '';
                response.matching.forEach(function(elem) {
                    matching += '<li>' + elem + '</li>';
                });

                $('#compare-added').html(added);
                $('#compare-removed').html(removed);
                $('#compare-matching').html(matching);
                $('#compare-header').html('Comparison [' + response.oldVersion +
                    ' <i class="caret right icon"></i>' + response.newVersion + ']');
                $('.ui.small.modal.compareResult')
                    .modal('show');
            }
        })
    ;

    $('#addStorageForm')
        .form({
            on : 'change',
            fields : {
                image : {
                    identifier : 'image',
                    rules : [
                        {
                            type    : 'empty',
                            prompt  : 'Missing storage image.'
                        }
                    ]
                },
                name : {
                    identifier : 'name',
                    rules : [
                        {
                            type    : 'empty',
                            prompt  : 'Missing storage name.'
                        }
                    ]
                },
                auditVersion : {
                    identifier : 'auditVersion',
                    rules : [
                        {
                            type    : 'empty',
                            prompt  : 'Missing initial version name'
                        }
                    ]
                }
            }
        }).submit(function() {
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
                success: function(response) {
                    var $formSubmission = $('#formSubmissionMsg');
                    $formSubmission.find('.header').text("Submission Successful");
                    $formSubmission.find('p').text("The object was successfully created!");
                    $formSubmission.removeClass('error');
                    $formSubmission.addClass("success");

                    var card = $('#storageCardTemplate').clone();
                    card.attr('id', '')
                        .appendTo('#storageCards')
                        .find(".header").html(response.name);

                    card.find(".image").click(function() {
                        location.href = window.location.href.split('?')[0] + '/' + response.name;
                    });

                    card.find('.description')
                        .html(response.description);

                    card.find(".imageURL").attr("src", response.imageURL + '?' + new Date().getTime());
                    card.find(".remove").attr("data", response.id);

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
                    $('#formSubmissionMsg').find('.header').text("Submission Failed");
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
        })
    ;

    var imgUploadForm = {
        on: 'submit',
        fields : {
            image : {
                identifier : 'image',
                rules: [
                    {
                        type    : 'empty',
                        prompt  : 'Please select an image to upload.'
                    }
                ]
            },
            name : {
                identifier : 'name',
                rules: [
                    {
                        type    : 'empty',
                        prompt  : 'Please select a name.'
                    }
                ]
            }
        }
    };

    $('#updateChemicalForm')
        .form(imgUploadForm).submit(function() {
            var url = $('#update-image-url').attr('value');
            var formData = new FormData($('#updateChemicalForm')[0]);

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
                success: function(response) {
                    var $formSubmission = $('#formSubmissionMsg');
                    $formSubmission.find('.header').text("Submission Successful");
                    $formSubmission.find('p').text("The object was successfully created!");
                    $formSubmission.removeClass('error');
                    $formSubmission.addClass("success");

                    $('img#' + response.name).attr("src", response.imageURL);
                },
                error: function() {
                    var $formSubmission = $('#formSubmissionMsg');
                    $formSubmission.find('.header').text("Submission Failed");
                    $formSubmission.find('p').text("An error occurred while creating object!");
                    $formSubmission.addClass("error");
                    $formSubmission.removeClass("success");
                },
                complete: function() {
                    $("#noStorages").hide();
                    $('#formSubmissionMsg').show();
                    $('.fixedLoader').removeClass('active');
                    //window.location = window.location.pathname;
                }
            });

        })
    ;

    $('#updateLocationForm')
        .form(imgUploadForm).submit(function() {
        var url = $('#update-image-url').attr('value');
        var formData = new FormData($('#updateLocationForm')[0]);

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
            success: function(response) {
                var $formSubmission = $('#formSubmissionMsg');
                $formSubmission.find('.header').text("Submission Successful");
                $formSubmission.find('p').text("The object was successfully created!");
                $formSubmission.removeClass('error');
                $formSubmission.addClass("success");

                $('img#' + response.name).attr("src", response.imageURL);
            },
            error: function() {
                var $formSubmission = $('#formSubmissionMsg');
                $formSubmission.find('.header').text("Submission Failed");
                $formSubmission.find('p').text("An error occurred while creating object!");
                $formSubmission.addClass("error");
                $formSubmission.removeClass("success");
            },
            complete: function() {
                $("#noStorages").hide();
                $('#formSubmissionMsg').show();
                $('.fixedLoader').removeClass('active');
                window.location = window.location.pathname;
            }
        });

    })
    ;

    $('#updateRoomForm')
        .form(imgUploadForm).submit(function() {
        var url = $('#update-image-url').attr('value');
        var formData = new FormData($('#updateRoomForm')[0]);

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
            success: function(response) {
                var $formSubmission = $('#formSubmissionMsg');
                $formSubmission.find('.header').text("Submission Successful");
                $formSubmission.find('p').text("The object was successfully created!");
                $formSubmission.removeClass('error');
                $formSubmission.addClass("success");

                $('img#' + response.name).attr("src", response.imageURL);
            },
            error: function() {
                var $formSubmission = $('#formSubmissionMsg');
                $formSubmission.find('.header').text("Submission Failed");
                $formSubmission.find('p').text("An error occurred while creating object!");
                $formSubmission.addClass("error");
                $formSubmission.removeClass("success");
            },
            complete: function() {
                $("#noStorages").hide();
                $('#formSubmissionMsg').show();
                $('.fixedLoader').removeClass('active');
                window.location = window.location.href.split("?")[0];
            }
        });

    })
    ;

    $('#updateCabinetForm')
        .form(imgUploadForm).submit(function() {
        var url = $('#update-image-url').attr('value');
        var formData = new FormData($('#updateCabinetForm')[0]);

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
            success: function(response) {
                var $formSubmission = $('#formSubmissionMsg');
                $formSubmission.find('.header').text("Submission Successful");
                $formSubmission.find('p').text("The object was successfully created!");
                $formSubmission.removeClass('error');
                $formSubmission.addClass("success");

                $('img#' + response.name).attr("src", response.imageURL);
            },
            error: function() {
                var $formSubmission = $('#formSubmissionMsg');
                $formSubmission.find('.header').text("Submission Failed");
                $formSubmission.find('p').text("An error occurred while creating object!");
                $formSubmission.addClass("error");
                $formSubmission.removeClass("success");
            },
            complete: function() {
                $("#noStorages").hide();
                $('#formSubmissionMsg').show();
                $('.fixedLoader').removeClass('active');
                window.location = window.location.href.split("?")[0];
            }
        });

    })
    ;

    $('.ui.search')
        .search({
            minCharacters : 3,
            action: 'search',
            fields: {
                results     : 'chemicals',
                title       : 'name',
                description : ''
            },
            apiSettings: {
                onResponse: function(response) {
                    var chemicals = [];
                    var source = response.hits.hits;
                    for (var i = 0; i < source.length; i++) {
                        var n = source[i]._source.name;
                        if (n === '' && n === null) {
                            n = source[i]._source['Name'];
                        }
                        chemicals.push({
                            name: n,
                            imageURL: 'https://s3-us-west-2.amazonaws.com/chemical-images/placeholder.png'
                        });
                    }

                    return {chemicals: chemicals};
                }
            },
            onSelect: function(chemical) {

                $.ajax({
                    type        : 'post',
                    dataType    : 'json',
                    contentType : 'application/json',
                    mimeType    : 'application/json',
                    url         : location.origin + '/api/update/chemical',
                    data        : JSON.stringify({
                        request         : 'ADD',
                        location        : $('#location-name').attr('value'),
                        room            : $('#room-name').attr('value'),
                        cabinet         : $('#cabinet-name').attr('value'),
                        auditVersion    : $('#audit-version').attr('value'),
                        chemical        : chemical.name
                    }),
                    beforeSend: function() {
                        $(".fixedLoader").addClass('active');
                    },
                    success: function(response) {
                        if (response.success) {
                            // TODO: simplify
                            var card = $('#chemicalCardTemplate').clone();
                            card.attr('id', '')
                                .appendTo('#chemicalCards')
                                .find(".header").html(chemical.name);

                            card.find(".image").click(function() {
                                location.href = window.location.href.split('?')[0] + '/' + chemical.name;
                            });

                            card.find(".imageURL").attr("src", chemical.imageURL);
                            card.find(".editChemical").attr("data", chemical.name);
                            card.find(".remove").attr("data", name);

                            card.find(".remove").click(function() {

                                var parentCard = $(this).parents().eq(2);

                                $('.ui.basic.modal.confirm')
                                    .modal({
                                        onApprove: function() {
                                            parentCard.remove();
                                            $.ajax({
                                                url: '/api/update/chemical',
                                                type: 'post',
                                                dataType: "json",
                                                contentType: "application/json",
                                                mimeType: "application/json",
                                                data: JSON.stringify({
                                                    request         : 'REMOVE',
                                                    location        : $('#location-name').attr('value'),
                                                    room            : $('#room-name').attr('value'),
                                                    cabinet         : $('#cabinet-name').attr('value'),
                                                    auditVersion    : $('#audit-version').attr('value'),
                                                    chemical        : chemical.name
                                                }),
                                                success: function(response) { },
                                                error: function(e) { }
                                            });
                                        }
                                    })
                                    .modal('show')
                                ;

                            });


                            $('.editChemical').click(function() {
                                var that = $(this);
                                $('.ui.small.modal.updateChemicalImage')
                                    .modal({
                                        onShow : function() {
                                            var chemicalName = that.attr('data')
                                            $(this).find('.header').text('Update Image for ' + chemicalName);
                                            $('#updateChemicalImageName').attr('value', chemicalName);
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

    $('#forkVersionForm')
        .form({
            on : 'change',
            fields : {
                forkVersion : {
                    identifier : 'forkVersion',
                    rules : [
                        {
                            type    : 'empty',
                            prompt  : 'Missing fork version name.'
                        }
                    ]
                }
            }
        }).submit(function() {
            var forkVersion = $('#forkVersion').val();
            if (forkVersion == '') {
                return;
            }

            $.ajax({
                type        : 'post',
                dataType    : 'json',
                contentType : 'application/json',
                mimeType    : 'application/json',
                url         : location.origin + '/api/update/cabinet',
                data        : JSON.stringify({
                    request         : 'FORK',
                    location        : $('#location-name').attr('value'),
                    room            : $('#room-name').attr('value'),
                    cabinet         : $('#cabinet-name').attr('value'),
                    auditVersion    : $('#audit-version').attr('value'),
                    forkVersion     : forkVersion,
                    forkWithChemicals : document.getElementById('forkWithChemicals').checked
                }),
                beforeSend: function() {
                    $(".fixedLoader").addClass('active');
                },
                success: function() {
                    // Set the name of the new version
                    $('#audit-version').attr('value')
                    location.reload();
                },
                complete: function() {
                    $('.fixedLoader').removeClass('active');
                }
            });
        })
    ;

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
                            url: '/api/update/chemical',
                            type: 'post',
                            dataType: "json",
                            contentType: "application/json",
                            mimeType: "application/json",
                            data: JSON.stringify({
                                request         : 'REMOVE',
                                location        : $('#location-name').attr('value'),
                                room            : $('#room-name').attr('value'),
                                cabinet         : $('#cabinet-name').attr('value'),
                                auditVersion    : $('#audit-version').attr('value'),
                                chemical        : chemicalName
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
});