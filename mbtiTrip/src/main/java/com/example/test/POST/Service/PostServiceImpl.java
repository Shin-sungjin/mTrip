package com.example.test.POST.Service;




import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.test.POST.DAO.AnswerDAO;
import com.example.test.POST.DAO.PostDAO;
import com.example.test.POST.DTO.AnswerDTO;
import com.example.test.POST.DTO.PostDTO;
import com.example.test.User.DTO.UserDTO;
import com.example.test.User.Service.UserHistoryService;
import com.example.test.item.DAO.ItemDAO;
import com.example.test.item.DTO.ItemDTO;
import com.example.test.paging.Page;
import com.example.test.paging.PaginationVo;
import com.example.testExcepion.Insert.InsertException;
import com.example.testExcepion.Insert.InsertExceptionEnum;
import com.example.testExcepion.Post.PostException;
import com.example.testExcepion.Post.PostExceptionEnum;
import com.example.testExcepion.updated.UpdateException;
import com.example.testExcepion.updated.UpdateExceptionEnum;

import groovy.util.logging.Log4j;
import lombok.extern.log4j.Log4j2;





@Log4j2
@Service
public  class PostServiceImpl implements PostService {

	@Autowired
	PostDAO postDAO;
	
	@Autowired
	AnswerDAO answerDAO;
	@Autowired
	ItemDAO itemDao;
	
	@Autowired
	UserHistoryService userHistoryService;






	
	@Override
	public List<PostDTO> getListPage(Page page) {
		// TODO Auto-generated method stub
		return this.postDAO.getListPage(page);
	}




	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return this.postDAO.getCount();
	}




	@Override
	public int create(PostDTO post)  {
		//postValidationCK(post);


			int result =postDAO.create(post);
			
			return result;

		 
	}

	@Override
	public PostDTO getPost(Integer postId,Principal principal) throws Exception {
		PostDTO post = this.postDAO.findById(postId);
		System.out.println(post.toString());      	
    	post.setViews(post.getViews()+1);        	
    	this.postDAO.update(post);
    	userHistoryService.userViewPost(post, principal);
        return post;
	        	
	}



	@Override
	public void modify(PostDTO post) throws Exception {
		postValidationCK(post);
		try {
			postDAO.update(post);
		}catch(Exception e) {
			throw new PostException(PostExceptionEnum.POST_UNABLE_TO_UPDATE);
		}
	}



	@Override
	public void remove(Integer postId) throws Exception {
		// TODO Auto-generated method stub
	
		try {
			postDAO.delete(postId);
		}catch(Exception e) {
			throw new PostException(PostExceptionEnum.POST_UNABLE_TO_DELETE);
		}
	}



	@Override
	public Integer totalCount() throws Exception {
		// TODO Auto-generated method stub
		return postDAO.totalCount();
	}

	//추천
	@Override
	public void suggestion(PostDTO postDto, UserDTO userDto) {
		//postDto.getSuggestion().add(userDto);
        
        this.postDAO.create(postDto);
	}

//    @Override
//	public List<PostDTO> findPostByCategoryID(PostDTO postDTO) {
//	// 게시글 목록 조회
//
//	List<PostDTO> post = postDAO.findByPostCategoryID(postDTO);
//	if(post.isEmpty()) {
//		throw new PostException(PostExceptionEnum.POST_NOT_FOUND);
//	}
//
//	return post;
//	        
//	}



	@Override
	public void replyRegister(AnswerDTO reply) throws Exception {
		if(reply.getWriter().getUsername() == null) {
			throw new PostException(PostExceptionEnum.POST_NOT_FOUND_USER);
		}
		if(reply.getContent().length()>500) {
			throw new PostException(PostExceptionEnum.POST_UNABLE_TO_ContentsSize);
		}
		try {
			postDAO.replyCreate(reply);
		}
		catch(Exception e){
			throw new PostException(PostExceptionEnum.POST_UNKNOWN_SEVER_ERROR);
		}
		
	}



	@Override
	public List<AnswerDTO> replyList(PostDTO postId) throws Exception {
		List<AnswerDTO> replyLi =  postDAO.replyList(postId);
		if(replyLi.isEmpty()) {
			throw new PostException(PostExceptionEnum.POST_NOT_FOUND);
		}
		return replyLi;
	}



	@Override
	public void replyModify(AnswerDTO reply) throws Exception {
		if(reply.getWriter().getUsername() == null) {
			throw new PostException(PostExceptionEnum.POST_NOT_FOUND_USER);
		}
		if(reply.getContent().length()>500) {
			throw new PostException(PostExceptionEnum.POST_UNABLE_TO_ContentsSize);
		}
		try {
			postDAO.replyUpdate(reply);
		} catch (Exception e) {
			throw new UpdateException(UpdateExceptionEnum.UPDATE_FAIL_SERVER);
		}
	}



	@Override
	public void replyRemove(Integer answerId) throws Exception {
		try {
			postDAO.replyDelete(answerId);	
		} catch (Exception e) {
			// TODO: handle exception
			throw new PostException(PostExceptionEnum.POST_UNABLE_TO_DELETE);
		}
		
	}



	private void postValidationCK(PostDTO postDTO) {
		if(postDTO.getUserName() == null) {
			throw new PostException(PostExceptionEnum.POST_PERMISSION_DENIED);
		}
		switch(titleCk(postDTO)) {
			case 0 : log.info("titleCK 타냐? ==>{}"); break;
			case 1 : throw new PostException(PostExceptionEnum.POST_UNABLE_TO_TITLE);
			case 2 : throw new PostException(PostExceptionEnum.POST_UNABLE_TO_TITLE3);
			case 3:  throw new PostException(PostExceptionEnum.POST_UNABLE_TO_TITLE2);
		}
		if(postDTO.getContent() == null) {
			throw new PostException(PostExceptionEnum.POST_UNABLE_TO_ContentsNULLPOINT);
		}
		if(postDTO.getContent().length() > 500) {
			throw new PostException(PostExceptionEnum.POST_UNABLE_TO_ContentsSize);
		}	
	}
	
	
	private int titleCk(PostDTO postDTO) {
		int check = 0;
		if(postDTO.getTitle() == null) {
			throw new PostException(PostExceptionEnum.POST_UNABLE_TO_TITLE4);
		}
		boolean ck = Pattern.matches("^[a-zA-Z0-9가-힣]*$", postDTO.getTitle());
		log.info("titleCk == > {}", postDTO.getTitle());
		if(!ck) {
			check=1;
		}
		if(postDTO.getTitle().length() > 15){
			check=2;
		}
		if(postDAO.titleCk(postDTO.getTitle()) != 0) {
			check = 3;
		}
		return check;
	}



	@Override
	public ItemDTO getItem(Integer itemID) {
		ItemDTO item = itemDao.findById(itemID);
		return item;
	}




	@Override
	public List<PostDTO> search(Page page) throws Exception {
		// TODO Auto-generated method stub
		return this.postDAO.search(page);
	}










	

	
	





	
	
    

}
