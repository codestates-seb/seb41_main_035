import styled from 'styled-components';

const Questionbar = () => {
  //   const [selectedDropValue, setSelectedDropValue] = useState([
  //     '상품을 선택하세요.',
  //   ]);
  return (
    <>
      <QWapper>
        <div className="Qb-contianer">
          <button className="image-upload">제품 이미지 업로드</button>
          <span className="category">카테고리칸</span>
        </div>
      </QWapper>
    </>
  );
};
const QWapper = styled.div`
  display: flex;
  justify-content: center;

  .Qb-contianer {
    width: 40%;
    height: 20vh;
    background-color: #d9d9d9;
  }
  .image-upload {
  }
  .category {
  }
`;
export default Questionbar;
