package com.swp.BabyandMom.Entity.Enum;

import io.swagger.v3.oas.annotations.media.Schema;
@Schema(enumAsRef = true)
public enum ReminderType {
    DOCTOR_APPOINTMENT,
    VACCINATION,
    MEDICAL_TEST,
    PRENATAL_CHECKUP,
    CUSTOM
}
