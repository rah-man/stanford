package cs.util;

public class Sentences {

    public static String sentences() {
        StringBuilder builder = new StringBuilder();
        builder.append("Offer a low-cost renin-angiotensin-aldosterone system antagonist to people with CKD and diabetes and an ACR of 3 mg/mmol or more (ACR category A2 or A3).\n");
        builder.append("Offer a low-cost renin-angiotensin-aldosterone system antagonist to people with CKD and hypertension and an ACR of 30 mg/mmol or more (ACR category A3).\n");
        builder.append("Offer a low-cost renin-angiotensin-aldosterone system antagonist to people with CKD and an ACR of 70 mg/mmol or more (irrespective of hypertension or cardiovascular disease).\n");
        builder.append("Control blood pressure to targets of 120-139/<90 mmHg in people without diabetes with ACR < 70 mg/mmol.\n");
        builder.append("Control blood pressure to targets of 120-129/<80 mmHg in people with diabetes or with ACR >= 70 mg/mmol.\n");
        builder.append("Offer colecalciferol or ergocalciferol to people who also have vitamin D deficiency if vitamin D supplementation is indicated in people with CKD.\n");
        builder.append("Offer alfacalcidol or calcitrol to people with GFR < 30 ml/min/1.73m2 if vitamin D supplementation is indicated in people with CKD and if vitamin D deficiency has been corrected and symptoms of CKD-mineral and bone disorders persist.");

        return builder.toString();
    }
}
