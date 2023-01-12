import styled from 'styled-components';
import { BsChatLeftText, BsPersonPlus } from 'react-icons/bs';
import Comment from '../components/Comment';

const PostView = () => {
  return (
    <SWapper>
      <SContainer>
        <div className="top_container">
          <SPost>
            <div className="outfit_upload"> 착용사진 나오는 칸</div>
            <span className="like_container"> 좋아요</span>
          </SPost>
          <SMiddle>
            <div className="user_info">
              <div className="user_box">
                <div className="user_id">아이디</div>
                <div className="icon">
                  <BsChatLeftText size="20" />
                  <BsPersonPlus size="20" />
                </div>
              </div>
              <div className="user_boxtwo">
                <div className="user_tall">키</div>
                <div className="user_weight"> 몸무게</div>
              </div>
            </div>
            <div className="post">게시물나오는 칸</div>
          </SMiddle>
        </div>
        <div className="botton_container">
          <div className="item_info">착용 제품 정보</div>

          <Comment />
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

  .botton_container {
    margin: 30px;
    .item_info {
      background-color: #f7e1ff;
      height: 100px;
      margin-bottom: 13px;
    }
  }
`;

const SPost = styled.div`
  .outfit_upload {
    width: 16vw;
    height: 31vh;
    background-color: #f2b9b9;
    display: flex;
    flex-direction: column;
    align-items: center;
    margin-right: 20px;
  }
  .like_container {
    margin-bottom: 10px;
    background-color: #faf4c5;
  }
`;

const SMiddle = styled.div`
  .user_info {
    display: flex;
    flex-direction: column;
  }
  .user_box {
    display: flex;
    flex-direction: row;
    margin: 5px;
    align-items: center;
    justify-content: space-between;
  }
  .user_id {
    margin-right: 20px;
  }

  svg {
    padding: 0 20px;
  }
  .user_boxtwo {
    display: flex;
    flex-direction: row;
    margin: 5px;
    align-items: center;
    justify-content: flex-start;
    .user_tall {
      margin-right: 10px;
    }
  }

  .post {
    width: 20vw;
    height: 25vh;
    background-color: #eaebba;
  }
`;
export default PostView;
