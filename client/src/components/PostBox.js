import styled from 'styled-components';
import dummyData from '../db/dummyData.json';
const PostBox = () => {
  return (
    <SWrapper>
      <Container>
        {dummyData.posts.map((post) => (
          <PostBoxOne key={post.id}>
            <div className="user-info ">
              <div className="user-info picture">사진</div>
              <div className="user-info name">{post.userNickname}</div>
            </div>
            <div className="style-picture">사진</div>
            <div className="add container">좋아요</div>
          </PostBoxOne>
        ))}
      </Container>
    </SWrapper>
  );
};
const SWrapper = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
`;
const Container = styled.div`
  width: 70%;
  display: flex;
  flex-wrap: wrap;
`;
const PostBoxOne = styled.div`
  width: 30%;
  height: 400px;
  background-color: #d9d9d9;
  display: flex;
  flex-direction: column;
  align-items: center;
  flex-wrap: wrap;
  margin: 30px 15px;

  .user-info {
    display: flex;
    padding-top: 8px;
    padding-bottom: 4px;
  }
  .style-picture {
    width: 250px;
    height: 300px;
    background-color: skyblue;
  }
`;

export default PostBox;
