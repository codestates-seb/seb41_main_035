import styled from 'styled-components';

const Comment = () => {
  return (
    <SWrapper>
      <form className="commentWrap">
        <input type="text" placeholder="댓글달기..." />

        <input type="text" placeholder="댓글달기..." />

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
