const { onDocumentCreated } = require("firebase-functions/v2/firestore");
const admin = require("firebase-admin");
const logger = require("firebase-functions/logger");
const nodemailer = require("nodemailer");
const { defineString } = require("firebase-functions/params");

admin.initializeApp();

const MAILTRAP_USER = defineString("MAILTRAP_USER");
const MAILTRAP_PASS = defineString("MAILTRAP_PASS");

exports.sendSkillAddedEmail = onDocumentCreated(
  "skills/{skillId}",
  async (event) => {
    const skill = event.data.data();
    const userId = skill.userId;

    const user = MAILTRAP_USER.value();
    const pass = MAILTRAP_PASS.value();

    logger.info("Mailtrap credentials (masked):", { user, pass: "****" });

    const transporter = nodemailer.createTransport({
      host: "smtp.mailtrap.io",
      port: 2525,
      auth: { user, pass },
    });

    try {
      const userRecord = await admin.auth().getUser(userId);
      const email = userRecord.email;

      const info = await transporter.sendMail({
        from: '"Skill App" <no-reply@example.com>',
        to: email,
        subject: `New skill added: ${skill.name}`,
        text: `Hi ${userRecord.displayName || "User"},\n\nYou just added a new skill: ${skill.name}. Congrats!`,
      });

      logger.info(`Email sent: ${info.messageId} to ${email}`);
    } catch (error) {
      logger.error("Error in sendSkillAddedEmail:", error);
    }
  }
);
