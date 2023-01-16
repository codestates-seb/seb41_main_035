import styled from 'styled-components';
import PostUploadBar from './PostUploadBar';
import ImageInput from '../components/ImageInput';

const PostUpload = () => {
  return (
    <Section>
      <Scontainer>
        <QHeader>
          <div className="image_upload">
            <button type="submit">완료</button>
          </div>
        </QHeader>

        <SMid>
          <ImageInput />
          <div className="input_box">
            <textarea
              placeholder="게시글을 작성하세요."
              // value={content}
              // onChange={(e) => setContent(e.target.value)}
            ></textarea>
          </div>
        </SMid>

        <PostUploadBar />
      </Scontainer>
    </Section>
  );
};

const Section = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;
`;
const Scontainer = styled.div`
  width: 70%;
  /* border: 1px solid green; */
`;

const QHeader = styled.div`
  display: flex;
  justify-content: center;
  /* border: 1px solid pink; */
  text-align: end;

  .image_upload {
    /* margin-left: 20px; */
    width: 38vw;
    margin-bottom: 10px;
    border-bottom: 1px solid #b3b3b3;
  }
  button {
    width: 3vw;
    height: 3vh;
    margin: 15px;
    background-color: #d9d4a6;
    color: #ffffff;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
`;

const SMid = styled.div`
  display: flex;
  justify-content: center;
  /* border: 1px solid pink; */
  /* width: 30vw; */
  .input_box {
    /* width: 34vw; */
    margin-bottom: 10px;
    display: flex;
    justify-content: end;
    /* background-color: #b3b3b3; */
  }
  textarea {
    font-size: 14px;
    box-sizing: border-box;
    min-height: 25vh;
    width: 23vw;
    border: none;
    border-radius: 5px;
    background-color: #ffffff;
    resize: none; /* 사용자가 텍스트사이즈 임의 변경 불가 */
  }
`;
export default PostUpload;
