import styled from 'styled-components';
import PostUploadBar from './PostUploadBar';

const PostUpload = () => {
  return (
    <Section>
      <QHeader>
        <div className="image_upload">
          {/* <button>이미지업로드</button> */}

          <input
            type="file"
            // multiple={true}
            // id="fileUpload"
            accept="image/png, image/jpeg"
          />
          <button type="submit">완료</button>
        </div>
      </QHeader>
      <div className="input_box">
        <textarea
          placeholder="게시글을 작성하세요."
          // value={content}
          // onChange={(e) => setContent(e.target.value)}
        />
      </div>
      <PostUploadBar />
    </Section>
  );
};

const Section = styled.div`
  .input_box {
    display: flex;
    justify-content: center;
    margin-bottom: 10px;
  }
  textarea {
    font-size: 14px;
    box-sizing: border-box;
    width: 40%;
    min-height: 30vh;
    border: none;
    border-radius: 3px;
    background-color: #ececec;
    resize: none; /* 사용자가 텍스트사이즈 임의 변경 불가 */
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
