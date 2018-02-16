/*
 * Copyright (C) 2012-2018 52°North Initiative for Geospatial Open Source
 * Software GmbH
 *
 * This program is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 as published
 * by the Free Software Foundation.
 *
 * If the program is linked with libraries which are licensed under one of
 * the following licenses, the combination of the program with the linked
 * library is not considered a "derivative work" of the program:
 *
 *     - Apache License, version 2.0
 *     - Apache Software License, version 1.0
 *     - GNU Lesser General Public License, version 3
 *     - Mozilla Public License, versions 1.0, 1.1 and 2.0
 *     - Common Development and Distribution License (CDDL), version 1.0
 *
 * Therefore the distribution of the program linked with libraries licensed
 * under the aforementioned licenses, is permitted by the copyright holders
 * if the distribution is compliant with both the GNU General Public
 * License version 2 and the aforementioned licenses.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
 * Public License for more details.
 */
!function ($) {

    function MqttConfigurationController(baseUrl) {
        var self = this;

        this.selectedConfig = {
            key: null,
            active: false,
            name: "",
            host: "",
            port: "",
            topic: "",
            protocol: "",
            username: "",
            password: "",
            decoder: "",
            observableProperties: new Array(),
            observationField: "",
            csvLineSeperator: "",
            csvFieldSeperator: ""
        };

        this.baseUrl = baseUrl;
        this.$configList = $("#mqttconf-list");

        this.$editButtons = $(".mqttconf-edit-button");
        this.$addNewButton = $("#mqttconf-addnew-button");
        this.$addNewOk = $("#mqttconf-add-new-form-ok");
        this.$addNewForm = $("#mqttconf-add-new-form");
        this.$saveButton = $("#mqttconf-save-button");
        this.$mqttConfPropsContainer = $("#mqtt-configuration-properties-container");

        this.$mqttNewName = $("#mqttconf-new-name-input");

        this.$mqttActivate = $("#mqttconf-activate-button");
        this.$mqttName = $("#mqttconf-name-input");
        this.$mqttHost = $("#mqttconf-host-input");
        this.$mqttPort = $("#mqttconf-port-input");
        this.$mqttTopic = $("#mqttconf-topic-input");
        this.$mqttProtocol = $("#mqttconf-protocol-select");
        this.$mqttDecoder = $("#mqttconf-decoder-select");

        this.bind();

    }
    $.extend(MqttConfigurationController.prototype, EventMixin);
    $.extend(MqttConfigurationController.prototype, {

        bind: function () {
            var self = this;
            var selectedConfig = self.$configList.find("option:selected").val();
            if (!self.$configList.val()) {
                self.$mqttConfPropsContainer.hide();
            } else {
                self.requestConfig(selectedConfig);
            }
            self.requestDecoders();

            this.$addNewButton.on("click", function () {
                self.$addNewButton.slideLeft(100, function () {
                    self.$addNewForm.slideRight();
                });
            });

            //show input for adding a new config
            $(".mqttconf-add-new-form-button").on("click", function () {
                self.$addNewForm.slideLeft(400, function () {
                    self.$addNewButton.slideRight(100);
                    self.$mqttNewName.val("").trigger("input");
                });
            });

            //prepare form for editting properties for a new config
            this.$addNewOk.on("click", function () {
                self.$mqttName.val(self.$mqttNewName.val());
                self.selectedConfig.name = self.$mqttNewName.val();
                self.cleanProperties();
                self.$mqttConfPropsContainer.slideDown("slow", function () {

                });
            });

            this.$configList.change(function () {
                var selectedConfig = self.$configList.find("option:selected");
                console.log(selectedConfig.val());
                self.requestConfig(selectedConfig.val());
            });

            this.$saveButton.on("click", function () {
                self.readProperties();

                if (self.selectedConfig.key) {
                    self.update(self.selectedConfig);
                } else {
                    self.create(self.selectedConfig);
                }

            });

            this.$mqttActivate.on("click", function () {
                var payload = {
                    mqttConfigurationKey: self.selectedConfig.key,
                    mqttConfigurationActivation: !self.selectedConfig.active
                };
                self.activateConfig(payload);
            });

        },
        create: function (payload) {
            var self = this;
            $.ajax({
                type: "POST",
                url: self.baseUrl + "admin/mqtt",
                contentType: "application/json",
                data: JSON.stringify(payload),
                context: this
            }).done(function (data) {
                showSuccess("MQTT configuration for " + data.name + " saved.");
                self.selectedConfig = data;
                self.$configList.append($('<option>', {
                    value: data.key,
                    text: data.name,
                    selected: true
                }));

            }).fail(function () {
                showError("MQTT configuration for " + payload.name + " could not be saved.");
            });
        },

        update: function (payload) {
            var self = this;
            $.ajax({
                type: "PUT",
                url: self.baseUrl + "admin/mqtt",
                contentType: "application/json",
                data: JSON.stringify(payload),
                context: this
            }).done(function (data) {
                showSuccess("MQTT configuration for " + data.name + " updated.");
                self.$configList.find("option:selected").text(self.selectedConfig.name);

            }).fail(function () {
                showError("MQTT configuration for " + payload.name + " could not be updated.");
            });
        },

        activateConfig: function (payload) {
            var self = this;
            self.$mqttActivate.prop("disabled", true);
            $.ajax({
                type: "POST",
                url: self.baseUrl + "admin/mqtt/operations",
                contentType: "application/json",
                data: JSON.stringify(payload),
                context: this
            }).done(function () {
                showSuccess("MQTT client " + (this.selectedConfig.active ? "activated" : "deactivated"));
                self.selectedConfig.active = !self.selectedConfig.active;
                self.$mqttActivate.toggleClass("btn-danger btn-success")
                        .text(self.selectedConfig.active ? "active" : "inactive")
                        .prop("disabled", false);
//                self.selectedConfig = data;
//                self.$configList.append($('<option>', {
//                    value: data.key,
//                    text: data.name,
//                    selected: true
//                }));

            }).fail(function () {
                showError("MQTT client could not be " + (payload.mqttConfigurationActivation ? "activated" : "deactivated"));
                self.$mqttActivate.prop("disabled", false);
            });
        },

        requestConfig: function (id) {
            var self = this;
            $.get(self.baseUrl + "admin/mqtt/" + id)
                    .done(function (data) {
                        console.log("Data loaded: " + data);
                        self.selectedConfig = data;
                        self.updateProperties(data);
                    }).fail(function () {
                showError("Data could not be loaded: ");
            });
        },

        requestDecoders: function () {
            var self = this;
            $.get(self.baseUrl + "admin/mqtt/decoders")
                    .done(function (data) {
                        console.log("Decoders loaded: " + data);
                        $.each(data, function (key, value) {
                            self.$mqttDecoder.append($('<option>', {
                                value: key,
                                text: value
                            }));
                        });
                    }).fail(function () {
                showError("Could not load available decoders.");
            });
        },

        readProperties: function () {
            this.selectedConfig.name = this.$mqttName.val();
            this.selectedConfig.host = this.$mqttHost.val();
            this.selectedConfig.port = this.$mqttPort.val();
            this.selectedConfig.topic = this.$mqttTopic.val();
            this.selectedConfig.protocol = this.$mqttProtocol.val();
            this.selectedConfig.decoder = this.$mqttDecoder.val();
        },

        updateProperties: function (data) {
            this.$mqttName.val(data.name);
            this.$mqttActivate.removeClass("btn-success btn-danger");
            this.$mqttActivate.addClass(this.selectedConfig.active ? "btn-success" : "btn-danger");
            this.$mqttActivate.text(this.selectedConfig.active ? "active" : "inactive");
            this.$mqttHost.val(data.host);
            this.$mqttPort.val(data.port);
            this.$mqttTopic.val(data.topic);
            this.$mqttProtocol.val(data.protocol);
            this.$mqttDecoder.val(data.decoder);
        },

        cleanProperties: function () {
            this.selectedConfig.key = null;
            this.selectedConfig.active = false;
            this.selectedConfig.host = "";
            this.$mqttHost.val("");
            this.selectedConfig.port = "";
            this.$mqttPort.val("");
            this.selectedConfig.topic = "";
            this.$mqttTopic.val("");
            this.selectedConfig.protocol = "tcp";
            this.$mqttProtocol.val("");
            this.selectedConfig.decoder = "org.n52.sos.mqtt.decode.AdsbDecoder";
            this.$mqttDecoder.val("");
        }

    });
    window.MqttConfigurationController = MqttConfigurationController;

}(jQuery);