package com.br.lojavirtual.model.dto;

import java.io.Serializable;
import java.util.ArrayList;

public class NewsLetterGetResponse implements Serializable {

	private static final long serialVersionUID = 1L;
	
	/*Conteúdo do e-mail em html ou texto*/
	private Content content = new Content();

	private ArrayList<String> flags = new ArrayList<String>();

	/*Nome do email no máximo 128 letras*/
	private String name;
	
	/*Tipo broadcast : transmissao ou draft : rascunho*/
	private String type = "broadcast";
	
	private String editor = "custom";

	/*O assunto do e-mail*/
	private String subject;
	
	/*email da pessoal que está enviado - remetente*/
	private FromField fromField = new FromField();
	
	/*email para endereco de resposta - destinatário*/
	private ReplyTo replyTo = new ReplyTo();
	
	/*Campanha na qual o e-mail é atribuido*/
	private LeadCampanhaGetResponseCadastro campaign = new LeadCampanhaGetResponseCadastro();
	
	/*Data de envio 2022-05-12T18:20:52-03:00*/
	private String sendOn;
	
	/*Os anexos e arquivos caso queira enviar*/
	private ArrayList<AttachmenteNewsLetterGetResponse> attachments = new ArrayList<AttachmenteNewsLetterGetResponse>();
	
	/*Configuraçoes extras*/
	private SendSettings sendSettings = new SendSettings();

	public Content getContent() {
		return content;
	}

	public void setContent(Content content) {
		this.content = content;
	}

	public ArrayList<String> getFlags() {
		return flags;
	}

	public void setFlags(ArrayList<String> flags) {
		this.flags = flags;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEditor() {
		return editor;
	}

	public void setEditor(String editor) {
		this.editor = editor;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public FromField getFromField() {
		return fromField;
	}

	public void setFromField(FromField fromField) {
		this.fromField = fromField;
	}

	public ReplyTo getReplyTo() {
		return replyTo;
	}

	public void setReplyTo(ReplyTo replyTo) {
		this.replyTo = replyTo;
	}

	public LeadCampanhaGetResponseCadastro getCampaign() {
		return campaign;
	}

	public void setCampaign(LeadCampanhaGetResponseCadastro campaign) {
		this.campaign = campaign;
	}

	public String getSendOn() {
		return sendOn;
	}

	public void setSendOn(String sendOn) {
		this.sendOn = sendOn;
	}

	public ArrayList<AttachmenteNewsLetterGetResponse> getAttachments() {
		return attachments;
	}

	public void setAttachments(ArrayList<AttachmenteNewsLetterGetResponse> attachments) {
		this.attachments = attachments;
	}

	public SendSettings getSendSettings() {
		return sendSettings;
	}

	public void setSendSettings(SendSettings sendSettings) {
		this.sendSettings = sendSettings;
	}
	

	
}
