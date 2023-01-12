import styled from 'styled-components';
import { useState, useRef } from 'react';
import { useNavigate } from 'react-router-dom';

// const [comment, setComment] = useState('');
// const [feedcomment, setFeedcommnet] = useState([]);

// const post = (e) => {
//   const copyfeedcomment = [...feedcomment];
//   copyfeedcomment.push(comment);
//   setFeedcommnet(copyfeedcomment);
//   setComment('');
//   // e.preventDefault();
// };
// const dummy = [
//   {
//     id: 1,
//     avatar: 'https://avatars.githubusercontent.com/u/111413253?v=4',
//     createdAt: '2022.12.24',
//     userNickname: 'tutu',
//     picture: 'https://avatars.githubusercontent.com/u/111413253?v=4',
//     content: 'I like pizza.',
//   },
//   {
//     id: 2,
//     avatar: 'https://avatars.githubusercontent.com/u/111413253?v=4',
//     createdAt: '2022.12.24',
//     userNickname: 'kfjdkfs',
//     picture: 'https://avatars.githubusercontent.com/u/111413253?v=4',
//     content: 'I like pizza.',
//   },
// ];
// const [msg, setMsg] = useState('');
// const [tweets, setTweets] = useState(dummy);

// const handleButtonClick = (event) => {
//   const tweet = {
//     content: msg,
//   };
//   const newTweets = [tweet, ...tweets];
//   setTweets(newTweets);
// };
// const handleChangeMsg = (e) => {
//   setMsg(e.target.value);
// };

// const [comment, setComment] = useState('');
// const [commentArray, setCommentArray] = useState([]);
// const onChange = (e) => {
//   setComment(e.target.value);
// };
// const onSubmit = (event) => {
//   event.preventDefault();
//   if (comment === '') {
//     return;
//   }
//   setCommentArray((commentValueList) => [comment, ...commentValueList]);
//   setComment('');
// };

// const contentRef = useRef();
// const [content, setContent] = useState('');

// const usenavigate = useNavigate();
// const handleSubmit = () => {
//   if (content.length < 1) {
//     contentRef.current.focus();
//     return;
//   }
//   usenavigate('/');
// };

const Comment = () => {
  return (
    <SWrapper>
      <form className="commentWrap">
        <input
          type="text"
          placeholder="댓글달기..."
          // value={comment}
          // onChange={onChange}
          // ref={contentRef}
          // value={content}
          // onChange={(e) => setContent(e.target.value)}
        />
        <button className="commetBtn">게시</button>
      </form>
    </SWrapper>
  );
};

const SWrapper = styled.div`
  width: 100%;

  .commentWrap {
    input {
      width: 84%;
      height: 4vh;
    }
    button {
      width: 10%;
      height: 4.5vh;
      margin-left: 20px;
    }
  }
`;
const commetBtn = styled.div`
  background-color: pink;
`;
export default Comment;
