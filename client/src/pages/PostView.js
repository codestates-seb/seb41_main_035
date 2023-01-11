import styled from 'styled-components';
import { BsChatLeftText, BsPersonPlus } from 'react-icons/bs';

const PostView = () => {
  return (
    <SWapper>
      <SContainer>
        <div className="top_container">
          <div className="outfit_upload"> 착용사진 나오는 칸 </div>
          <SMiddle>
            <div className="user_info">
              <div className="user_box">
                <div className="user_id">아이디</div>
                <BsChatLeftText />
                <BsPersonPlus />
              </div>
              <div className="user_box">
                <div className="user_tall">키</div>
                <div className="user_weight"> 몸무게</div>
              </div>
            </div>
            <div className="post">게시물나오는 칸</div>
          </SMiddle>
        </div>
      </SContainer>
    </SWapper>
  );
};

const SWapper = styled.div`
  display: flex;
  justify-content: center;
  width: 100%;
`;
const SContainer = styled.div`
  width: 40%;
  height: 600px;
  border: 1px solid gray;

  .top_container {
    display: flex;
    margin: 30px;
  }
  .outfit_upload {
    width: 400px;
    height: 300px;
    background-color: #f2b9b9;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-right: 20px;
  }
  .user_info {
    display: flex;
    flex-direction: column;
  }
  .user_box {
    display: flex;
    flex-direction: row;
    margin: 5px;
    align-items: center;
    .user_tall {
      margin-right: 10px;
    }
  }
  .post {
    width: 350px;
    height: 250px;
    background-color: #eaebba;
  }
`;
const SMiddle = styled.div``;
export default PostView;
