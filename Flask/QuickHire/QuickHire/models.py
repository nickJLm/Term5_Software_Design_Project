from QuickHire import db
from sqlalchemy.dialects import mysql
from sqlalchemy import Column, DateTime, String, Integer, ForeignKey, func
from sqlalchemy.orm import relationship, backref
from sqlalchemy.ext.declarative import declarative_base
from passlib.hash import pbkdf2_sha256 as sha256 

class UserModel(db.Model):
    __tablename__ = 'users'

    id = db.Column(db.Integer, primary_key = True)
    username = db.Column(db.String(120), unique = True, nullable = False)
    password = db.Column(db.String(120), nullable = False)
    
    @classmethod
    def generate_hash(cls, password):
        return sha256.hash(password)    
    
    @staticmethod
    def verify_hash(password, hash):
        return sha256.verify(password, hash)

    @classmethod
    def find_by_username(cls, username):
		return cls.query.filter_by(username = username).first()

    def save_to_db(self):
        db.session.add(self)
        db.session.commit()

    @classmethod
    def getID(cls, username):
    	return cls.query.filter_by(username = username).first().id


################

	

class PostingModel(db.Model):
	__tablename__ = 'postings'
	id = db.Column(db.Integer, primary_key = True)
	access_key = db.Column(Integer, nullable=False)
	owner_id = db.Column(Integer, nullable=False)
	description = db.Column(mysql.LONGTEXT, nullable=True)
	title = db.Column(mysql.TEXT, nullable=False)
	company_name = db.Column(mysql.VARCHAR(100))

	def save_to_db(self):
		db.session.add(self)
		db.session.commit()

	def getPostingFromAccessCode(self,code):
	    return self.query.filter_by(access_key = code).first()

	def keyExists(self , key):
		return self.query.filter_by(access_key = key).first()




################

class QuestionModel(db.Model):
	__tablename__='questions'
	id = Column(Integer, primary_key = True)
	posting_id = Column(Integer,db.ForeignKey(PostingModel.id),nullable=True)
	description = db.Column(mysql.TEXT)


###############


class TextQuestionModel(db.Model):
	__tablename__='textQuestions'
	id = Column(Integer, primary_key = True)
	posting_id = Column(Integer,db.ForeignKey(PostingModel.id),nullable=True)
	question = db.Column(mysql.TEXT)
	is_multiple_choice = Column(Integer, default=0)
	answer_type = Column(Integer, default=0)
	answer_restriction = db.Column(mysql.BIGINT , default=0)

	def save_to_db(self):
		db.session.add(self)
		db.session.commit()

	
	def getTextQuestionsFromPostId(self,postID):
		return self.query.filter_by(posting_id = postID).all()
		


class MultipleChoiceOptionModel(db.Model):
	__tablename__='multipleChoiceOptions'
	id = Column(Integer, primary_key = True)
	question_id = Column(Integer,db.ForeignKey(TextQuestionModel.id),nullable=True)
	option = db.Column(mysql.TEXT)

	def save_to_db(self):
		db.session.add(self)
		db.session.commit()

	def getOptionsFromId(self,id):
		return self.query.filter_by(question_id=id).all()
		


###############