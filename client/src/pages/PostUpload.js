import styled from 'styled-components';
import PostUploadBar from './PostUploadBar';
// import ImageInput from '../components/ImageInput';
const PostUpload = () => {
  return (
    <Section>
      <Scontainer>
        <QHeader>
          <div className="image_upload">
            {/* <button>이미지업로드</button> */}
            <input
              type="file"
              // multiple={true}
              // id="fileUpload"
              accept="image/png, image/jpeg"
            />
            {/* <ImageInput /> */}
            <button type="submit">완료</button>
          </div>
        </QHeader>
        <div className="input_box">
          <textarea
            placeholder="게시글을 작성하세요."
            // value={content}
            // onChange={(e) => setContent(e.target.value)}
          ></textarea>
        </div>
        <PostUploadBar />
      </Scontainer>
    </Section>
  );
};

const Section = styled.div`
  width: 100%;
  display: flex;
  justify-content: center;

  textarea {
    font-size: 14px;
    box-sizing: border-box;
    min-height: 30vh;
    width: 55%;
    border: none;
    border-radius: 3px;
    background-color: #ececec;
    resize: none; /* 사용자가 텍스트사이즈 임의 변경 불가 */
  }
`;
const Scontainer = styled.div`
  width: 70%;
  /* border: 1px solid green; */
  .input_box {
    display: flex;
    justify-content: center;
    margin-bottom: 10px;
  }
`;

const QHeader = styled.div`
  display: flex;
  justify-content: center;

  .image_upload {
  }
  button {
    width: 6vw;
    height: 3vh;
    margin: 15px;
  }
`;
export default PostUpload;
